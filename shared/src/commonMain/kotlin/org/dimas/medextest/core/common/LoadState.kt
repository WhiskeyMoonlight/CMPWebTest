package org.dimas.medextest.core.common

sealed interface LoadState<out T> {
    data object Loading : LoadState<Nothing>
    data class Content<out T>(val data: T) : LoadState<T>
    data object Empty : LoadState<Nothing>
    data class Error(val error: AppError) : LoadState<Nothing>
}
