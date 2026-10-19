package org.dimas.medextest.testutil

import kotlin.js.js

internal fun startFrameProbe(): Unit = js(
    """(function () {
        var probe = { last: 0, worst: 0, frames: 0, running: true };
        globalThis.__medexFrameProbe = probe;
        function tick(time) {
            if (!probe.running) return;
            if (probe.last > 0) { probe.worst = Math.max(probe.worst, time - probe.last); probe.frames++; }
            probe.last = time;
            requestAnimationFrame(tick);
        }
        requestAnimationFrame(tick);
    })()""",
)

internal fun stopFrameProbe(): Double = js(
    "(function () { var probe = globalThis.__medexFrameProbe; probe.running = false; return probe.worst; })()",
)

internal fun frameProbeFrames(): Int = js("globalThis.__medexFrameProbe.frames")
