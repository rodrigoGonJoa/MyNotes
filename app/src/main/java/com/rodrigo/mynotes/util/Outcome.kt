package com.rodrigo.mynotes.util

sealed class Outcome<out T: Any> {
    data class Success<out T: Any>(val value: T): Outcome<T>()
    data class Error(val message: String? = null, val cause: Exception? = null): Outcome<Nothing>()
}