package com.rodrigo.mynotes.domain.model

import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.utils.StateType

sealed class UiState<out T> {
    data class LoadingState<T>(val loading: Boolean): UiState<T>()
    data class SuccessState<T>(
        val value: T,
        val message: String,
        val type: StateType = StateType.Unknown,
    ): UiState<T>()

    data class ErrorState(
        val message: String,
        val cause: Exception? = null,
        val type: StateType = StateType.Unknown,
    ): UiState<Nothing>()
}

fun <T> UiState.SuccessState<T>.toDataState(): DataState.SuccessState<T> {
    return DataState.SuccessState(value = value, message = message, type = type)
}

fun UiState.ErrorState.toDataState(): DataState.ErrorState {
    return DataState.ErrorState(message = message, cause = cause, type = type)
}