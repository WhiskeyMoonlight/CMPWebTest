package org.dimas.medextest.feature.more.loadlab.debug

import kotlin.js.JsAny

external interface PerfBridgeApi : JsAny {
    var run: JsAny?
    var tasks: JsAny?
    var clear: JsAny?
}
