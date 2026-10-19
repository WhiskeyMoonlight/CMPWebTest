package org.dimas.medextest.compute.worker

object WorkerMessageKind {
    const val JOB = "job"
    const val LAB_PAGE = "lab"
    const val BYTES = "bytes"
    const val READY = "ready"
    const val PROGRESS = "progress"
    const val DONE = "done"
    const val ERROR = "error"
}
