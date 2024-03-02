package com.rodrigo.mynotes.util

sealed class StateType {
    data object Unknown: StateType()
    data object Added: StateType()
    data object Updated: StateType()
    data object Deleted: StateType()
    data object Obtained: StateType()
}