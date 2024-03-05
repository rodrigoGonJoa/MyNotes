package com.rodrigo.mynotes.presentation.note_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NoteListScreen(
    viewmodel: NoteListViewModel = hiltViewModel()
){
    val state by viewmodel.state.collectAsState()
    val message = state.notificationMessage
    val loadingVisibility = state.loading
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if (loadingVisibility) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "notification message = $message")

            Button(onClick = {viewmodel.getNotes()}) {
                Text(text = "Get Notes")
            }
            Button(onClick = {viewmodel.deleteNote(29L)}) {
                Text(text = "Delete note 5")
            }
            Text(text = "notes = ${state.notesToString()}")
        }
    }
}