package org.dimas.medextest.core.platform

import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidPermissionRequester {
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private var pending: CompletableDeferred<Map<String, Boolean>>? = null

    suspend fun request(permissions: Array<String>): Map<String, Boolean> = withContext(Dispatchers.Main.immediate) {
        val activeLauncher = checkNotNull(launcher) { "Permission launcher is not attached to an Activity" }
        pending?.cancel()
        val result = CompletableDeferred<Map<String, Boolean>>()
        pending = result
        activeLauncher.launch(permissions)
        result.await()
    }

    internal fun attach(launcher: ActivityResultLauncher<Array<String>>) {
        this.launcher = launcher
    }

    internal fun detach(launcher: ActivityResultLauncher<Array<String>>) {
        if (this.launcher === launcher) this.launcher = null
    }

    internal fun onResult(result: Map<String, Boolean>) {
        pending?.complete(result)
        pending = null
    }
}
