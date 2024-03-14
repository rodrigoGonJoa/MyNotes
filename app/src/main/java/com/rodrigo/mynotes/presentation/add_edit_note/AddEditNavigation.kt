package com.rodrigo.mynotes.presentation.add_edit_note

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.rodrigo.mynotes.presentation.navigation.NavArg

@Composable
fun AddEditNavigation(navController: NavController, bundle: Bundle?) {
    val id = bundle?.getLong(NavArg.NoteId.key)
    AddEditScreen(
        noteIdFromNoteList = id,
        onClickBack = {navController.popBackStack()}
    )
}