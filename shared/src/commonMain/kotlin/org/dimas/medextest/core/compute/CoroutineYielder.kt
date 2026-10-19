package org.dimas.medextest.core.compute

import kotlinx.coroutines.yield

class CoroutineYielder : UiYielder {
    override suspend fun yieldToUi() = yield()
}
