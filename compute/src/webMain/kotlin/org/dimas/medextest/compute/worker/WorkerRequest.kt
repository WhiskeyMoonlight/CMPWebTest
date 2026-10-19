package org.dimas.medextest.compute.worker

import js.buffer.ArrayBuffer
import kotlin.js.JsAny

external interface WorkerRequest : JsAny {
    var id: Int
    var kind: String
    var payload: String?
    var buffer: ArrayBuffer?
}
