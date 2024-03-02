package com.rodrigo.mynotes.presentation.note_list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoteListScreen(
    viewmodel: NoteListViewModel = hiltViewModel()
){
    val state by viewmodel.state.collectAsState()

    Text(text = state.notificationMessage)
}