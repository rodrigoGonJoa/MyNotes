package com.rodrigo.mynotes.util

sealed class ErrorHandler<T>(val data: T? = null, val message: String? = null){
    class Success<T>(message: String? = null, data: T? = null): ErrorHandler<T>(data)
    class Error<T>(message: String? = null, data: T? = null): ErrorHandler<T>(data, message)
}