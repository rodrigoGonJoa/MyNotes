package com.rodrigo.mynotes.presentation.note_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.presentation.composables.NoteItemComposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    navigateToNote: (Long?) -> Unit
) {
    val notes = viewModel.notes.collectAsState()
    val notificationMessage = viewModel.notificationMessage.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val successfulAction = viewModel.successfulAction.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToNote(null)}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = {

        }

    ) {innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true
        ) {
            items(notes.value) {note ->
                NoteItemComposable(note = note, onClickNote = {navigateToNote(note.id)})
            }
        }
    }
}