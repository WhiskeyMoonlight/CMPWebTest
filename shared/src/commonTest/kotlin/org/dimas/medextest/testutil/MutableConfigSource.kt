package org.dimas.medextest.testutil

import kotlinx.coroutines.flow.MutableStateFlow
import org.dimas.medextest.core.mock.MockBackendConfig
import org.dimas.medextest.core.mock.MockBackendConfigSource

class MutableConfigSource(initial: MockBackendConfig) : MockBackendConfigSource {
    override val config = MutableStateFlow(initial)
}
