package org.dimas.medextest.compute.worker

import kotlin.js.JsAny

external interface WorkerResponse : JsAny {
    var id: Int
    var kind: String
    var steps: Int
    var payload: String?
}
