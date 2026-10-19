package org.dimas.medextest.core.common

sealed interface AppError {
    data object Network : AppError
    data class Unexpected(val message: String?) : AppError
}
