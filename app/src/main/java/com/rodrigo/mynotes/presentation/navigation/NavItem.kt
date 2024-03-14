package com.rodrigo.mynotes.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    protected val baseRoute: String,
    private val navArgs: List<NavArg> = emptyList()
) {
    data object AddEditScreen: NavItem("AddEditScreen", listOf(NavArg.NoteId)) {
        fun createNavRoute(noteId: Long) = "$baseRoute/$noteId"
    }

    data object NoteListScreen: NavItem("NoteListScreen")

    val route = run {
        val argKeys = navArgs.map {"{${it.key}}"}
        listOf(baseRoute)
            .plus(argKeys)
            .joinToString("/")
    }
    val args = navArgs.map {
        navArgument(it.key) {type = it.navType}
    }
}

enum class NavArg(val key: String, val navType: NavType<*>) {
    NoteId("noteId", NavType.LongType)
}