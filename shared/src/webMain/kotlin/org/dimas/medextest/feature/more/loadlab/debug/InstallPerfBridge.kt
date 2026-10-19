package org.dimas.medextest.feature.more.loadlab.debug

import js.objects.unsafeJso
import js.reflect.unsafeCast
import org.koin.mp.KoinPlatform
import web.location.location
import kotlin.js.JsAny
import kotlin.js.JsString
import kotlin.js.js
import kotlin.js.toJsNumber
import kotlin.js.toJsString

private const val BRIDGE_QUERY = "perf-bridge"

fun installPerfBridgeIfRequested() {
    if (BRIDGE_QUERY !in location.search) return
    val koin = KoinPlatform.getKoin()
    val bridge = LoadLabPerfBridge(backgroundTasks = koin.get(), runScenario = koin.get())
    val run: (JsString) -> JsAny? = { spec -> bridge.run(spec.toString()).toJsNumber() }
    val tasks: () -> JsAny? = { bridge.tasksJson().toJsString() }
    val clear: () -> Unit = { bridge.clearFinished() }
    exposePerfBridge(
        unsafeJso<PerfBridgeApi> {
            this.run = unsafeCast(run)
            this.tasks = unsafeCast(tasks)
            this.clear = unsafeCast(clear)
        },
    )
}

private fun exposePerfBridge(api: PerfBridgeApi): Unit = js("globalThis.medexPerfBridge = api")
