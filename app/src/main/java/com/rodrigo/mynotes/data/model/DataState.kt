package com.rodrigo.mynotes.data.model

import com.rodrigo.mynotes.domain.model.UiState

sealed class DataState<out T> {
    data class SuccessState<T>(val value: T, val message: String): DataState<T>()
    data class ErrorState(val message: String, val cause: Exception? = null): DataState<Nothing>()
}
fun <T> DataState.SuccessState<T>.toUiState(): UiState.SuccessState<T> {
    return UiState.SuccessState(value, message)
}
fun DataState.ErrorState.toUiState(): UiState.ErrorState {
    return UiState.ErrorState(message, cause)
}