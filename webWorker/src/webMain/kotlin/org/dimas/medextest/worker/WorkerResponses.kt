package org.dimas.medextest.worker

import js.objects.unsafeJso
import org.dimas.medextest.compute.worker.WorkerResponse

internal fun workerResponse(id: Int, kind: String, steps: Int = 0, payload: String? = null): WorkerResponse =
    unsafeJso {
        this.id = id
        this.kind = kind
        this.steps = steps
        this.payload = payload
    }
