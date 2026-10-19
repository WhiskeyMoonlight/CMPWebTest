package org.dimas.medextest.core.mock

import kotlinx.coroutines.flow.StateFlow

interface MockBackendConfigSource {
    val config: StateFlow<MockBackendConfig>
}
