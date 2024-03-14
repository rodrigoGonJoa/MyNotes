package com.rodrigo.mynotes.presentation.note_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodrigo.mynotes.domain.model.Note

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    navigateToNote: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val notificationMessage = state.notificationMessage
    val snackbarHostState = remember {SnackbarHostState()}
    val buttonTapped = remember {mutableStateOf(true)}

    LaunchedEffect(notificationMessage, buttonTapped.value) {
        notificationMessage?.let {message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            floatingActionButton = {
                FloatingActionButton(onClick = {navigateToNote(-1L)}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            },
            snackbarHost = {SnackbarHost(snackbarHostState)},
        ) {innerPadding ->
            NoteList(state = state, innerPadding = innerPadding, navigateToNote = navigateToNote)
        }
    }
}

@Composable
private fun NoteList(
    state: NoteListState,
    innerPadding: PaddingValues,
    navigateToNote: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = true
    ) {
        items(state.notes) {note ->
            NoteItemComposable(note = note, onClickNote = {navigateToNote(note.id!!)})
        }
    }

    if (state.loading) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoteItemComposable(note: Note, onClickNote: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClickNote)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = note.content,
                maxLines = 4,
                minLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}