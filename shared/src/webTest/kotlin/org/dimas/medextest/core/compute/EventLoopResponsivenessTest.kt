package org.dimas.medextest.core.compute

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.dimas.medextest.compute.job.HashJob
import org.dimas.medextest.testutil.RecordingProgress
import org.dimas.medextest.testutil.frameProbeFrames
import org.dimas.medextest.testutil.startFrameProbe
import org.dimas.medextest.testutil.stopFrameProbe
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

class EventLoopResponsivenessTest {
    private class Measurement(val elapsed: Duration, val worstTimerLag: Duration, val worstFrameGapMs: Double, val frames: Int) {
        override fun toString() = "работа ${elapsed.inWholeMilliseconds} мс, лаг таймера ${worstTimerLag.inWholeMilliseconds} мс, " +
            "худший кадр ${worstFrameGapMs.toInt()} мс, кадров $frames"
    }

    private fun executor(yielder: UiYielder) = ComputeExecutor(
        mainDispatcher = Dispatchers.Default,
        defaultDispatcher = Dispatchers.Default,
        uiYielder = yielder,
        workers = NoWorkerBackend,
        cpuCores = 1,
    )

    private suspend fun measure(work: suspend () -> Unit): Measurement = withContext(Dispatchers.Default) {
        coroutineScope {
            var worstLag = Duration.ZERO
            startFrameProbe()
            delay(PROBE_INTERVAL * 3)
            val probe = launch {
                var mark = TimeSource.Monotonic.markNow()
                while (isActive) {
                    delay(PROBE_INTERVAL)
                    worstLag = maxOf(worstLag, mark.elapsedNow() - PROBE_INTERVAL)
                    mark = TimeSource.Monotonic.markNow()
                }
            }
            delay(PROBE_INTERVAL)
            val started = TimeSource.Monotonic.markNow()
            work()
            val elapsed = started.elapsedNow()
            delay(PROBE_INTERVAL * 2)
            probe.cancel()
            Measurement(elapsed, worstLag, stopFrameProbe(), frameProbeFrames())
        }
    }

    @Test
    fun blocking_work_on_default_dispatcher_freezes_timers_and_frames() = runTest {
        val measurement = measure {
            executor(MessageChannelYielder()).execute(WORK, ExecutionMode.Default, RecordingProgress())
        }
        println("Default (блокирует поток): $measurement")

        assertTrue(measurement.elapsed > 150.milliseconds, "работа слишком короткая: $measurement")
        assertTrue(measurement.worstTimerLag >= measurement.elapsed * 0.8, measurement.toString())
        assertTrue(measurement.worstFrameGapMs >= measurement.elapsed.inWholeMilliseconds * 0.8, measurement.toString())
    }

    @Test
    fun sliced_work_with_message_channel_keeps_event_loop_responsive() = runTest {
        val measurement = measure {
            executor(MessageChannelYielder()).execute(WORK, ExecutionMode.Cooperative, RecordingProgress())
        }
        println("Нарезка + MessageChannel: $measurement")

        assertTrue(measurement.worstTimerLag < 60.milliseconds, measurement.toString())
        assertTrue(measurement.worstFrameGapMs < 60.0, measurement.toString())
    }

    @Test
    fun sliced_work_with_plain_yield_is_compared_to_message_channel() = runTest {
        val withYield = measure {
            executor(CoroutineYielder()).execute(WORK, ExecutionMode.Cooperative, RecordingProgress())
        }
        val withMessageChannel = measure {
            executor(MessageChannelYielder()).execute(WORK, ExecutionMode.Cooperative, RecordingProgress())
        }
        println("Нарезка + yield(): $withYield")
        println("Нарезка + MessageChannel: $withMessageChannel")

        assertTrue(withMessageChannel.worstFrameGapMs <= withYield.worstFrameGapMs + FRAME_TOLERANCE_MS, "$withYield vs $withMessageChannel")
    }

    @Test
    fun ui_yield_strategies_have_different_cost() = runTest {
        val yielder = MessageChannelYielder()
        val costs = withContext(Dispatchers.Default) {
            mapOf(
                "yield()" to time { repeat(YIELDS) { yield() } },
                "delay(1)" to time { repeat(YIELDS) { delay(1) } },
                "MessageChannel" to time { repeat(YIELDS) { yielder.yieldToUi() } },
            )
        }
        println("Стоимость $YIELDS уступок: " + costs.entries.joinToString { (name, cost) -> "$name = ${cost.inWholeMilliseconds} мс" })

        assertTrue(costs.getValue("delay(1)") >= YIELDS.milliseconds, costs.toString())
        assertTrue(costs.getValue("MessageChannel") < costs.getValue("delay(1)"), costs.toString())
    }

    private suspend fun time(block: suspend () -> Unit): Duration {
        val started = TimeSource.Monotonic.markNow()
        block()
        return started.elapsedNow()
    }

    private companion object {
        val WORK = HashJob(seed = 7, sizeBytes = 64 * 1024 * 1024)
        val PROBE_INTERVAL = 10.milliseconds
        const val YIELDS = 200
        const val FRAME_TOLERANCE_MS = 20.0
    }
}
