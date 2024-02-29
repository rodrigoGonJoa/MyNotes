package com.rodrigo.mynotes.util

sealed class UiState<out T> {
    data class LoadingState<T>(val loading: Boolean) : UiState<T>()
    data class SuccessState<T>(val value: T, val message: String): UiState<T>()
    data class ErrorState(val message: String, val cause: Exception? = null): UiState<Nothing>()
}

fun <T> UiState.SuccessState<T>.toDataState(): DataState.SuccessState<T> {
    return DataState.SuccessState(value, message)
}

fun UiState.ErrorState.toDataState(): DataState.ErrorState {
    return DataState.ErrorState(message, cause)
}