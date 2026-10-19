package org.dimas.medextest.core.common

inline fun <T> Result<T>.toLoadState(isEmpty: (T) -> Boolean = { false }): LoadState<T> = fold(
    onSuccess = { data -> if (isEmpty(data)) LoadState.Empty else LoadState.Content(data) },
    onFailure = { error -> LoadState.Error(error.toAppError()) },
)
