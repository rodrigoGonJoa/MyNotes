package com.rodrigo.mynotes.data.model

import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.utils.StateType

sealed class DataState<out T> {
    data class SuccessState<T>(
        val value: T,
        val type: StateType = StateType.Unknown,
        val message: String
    ): DataState<T>()

    data class ErrorState(
        val type: StateType = StateType.Unknown,
        val message: String,
        val cause: Exception? = null
    ): DataState<Nothing>()
}

fun <T> DataState.SuccessState<T>.toUiState(): UiState.SuccessState<T> {
    return UiState.SuccessState(value = value, message = message, type = type)
}

fun DataState.ErrorState.toUiState(): UiState.ErrorState {
    return UiState.ErrorState(message = message, cause = cause, type = type)
}