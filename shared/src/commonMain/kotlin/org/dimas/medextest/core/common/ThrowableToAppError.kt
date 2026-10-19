package org.dimas.medextest.core.common

fun Throwable.toAppError(): AppError = when (this) {
    is NetworkException -> AppError.Network
    else -> AppError.Unexpected(message)
}
