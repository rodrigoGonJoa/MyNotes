package com.rodrigo.mynotes.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodrigo.mynotes.presentation.add_edit_note.AddEditNavigation
import com.rodrigo.mynotes.presentation.note_list.NoteListNavigation

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavItem.NoteListScreen.route,
        enterTransition = {EnterTransition.None},
        exitTransition = {ExitTransition.None}
    ) {
        composable(
            route = NavItem.NoteListScreen.route,
            arguments = NavItem.NoteListScreen.args
        ) {entry -> NoteListNavigation(navController, entry.arguments)}
        composable(
            route = NavItem.AddEditScreen.route,
            arguments = NavItem.AddEditScreen.args
        ) {entry -> AddEditNavigation(navController, entry.arguments)}
    }
}