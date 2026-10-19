package org.dimas.medextest.testutil

import org.orbitmvi.orbit.OrbitContainerHost
import org.orbitmvi.orbit.test.OrbitScopedTestContextInternal

suspend fun <S : Any, E : Any, SE : Any, H : OrbitContainerHost<S, E, SE>> OrbitScopedTestContextInternal<S, E, SE, H>.awaitStateMatching(
    predicate: (S) -> Boolean,
): S {
    while (true) {
        val state = awaitInternalState()
        if (predicate(state)) return state
    }
}
