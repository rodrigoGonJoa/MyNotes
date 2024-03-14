package com.rodrigo.mynotes.presentation.note_list

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.rodrigo.mynotes.presentation.navigation.NavItem


@Composable
fun NoteListNavigation(navController: NavController, bundle: Bundle?) {
    NoteListScreen(
        navigateToNote = {noteId ->
            navController.navigate(NavItem.AddEditScreen.createNavRoute(noteId))
        }
    )
}