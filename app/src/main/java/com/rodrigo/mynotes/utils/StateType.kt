package com.rodrigo.mynotes.utils

sealed class StateType {
    data object Unknown: StateType()
    data object Add: StateType()
    data object Update: StateType()
    data object Delete: StateType()
    data object Obtain: StateType()
}