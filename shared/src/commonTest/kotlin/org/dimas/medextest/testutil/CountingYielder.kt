package org.dimas.medextest.testutil

import kotlinx.coroutines.yield
import org.dimas.medextest.core.compute.UiYielder

class CountingYielder(private val onYield: (Int) -> Unit = {}) : UiYielder {
    var yields = 0
        private set

    override suspend fun yieldToUi() {
        yields++
        onYield(yields)
        yield()
    }
}
