package com.rodrigo.mynotes.presentation.note_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.presentation.composables.DeleteNoteDialog

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(),
    navigateToNote: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val notificationMessage = state.notificationMessage
    val snackbarHostState = remember {SnackbarHostState()}
    val buttonTapped = remember {mutableStateOf(true)}
    val showDeleteDialog = remember {mutableStateOf(false)}
    val noteToDelete = remember {mutableLongStateOf(0L)}

    LaunchedEffect(notificationMessage, buttonTapped.value) {
        notificationMessage?.let {message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (showDeleteDialog.value) {
        DeleteNoteDialog(
            showDialog = showDeleteDialog,
            deleteAction = {
                viewModel.onEvent(NoteListEvent.deleteNote(noteToDelete.longValue))
            }
        )
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
            NoteList(
                state = state,
                innerPadding = innerPadding,
                navigateToNote = navigateToNote,
                noteToDelete = noteToDelete,
                showDeleteDialog = showDeleteDialog
            )
        }
    }
}

@Composable
private fun NoteList(
    state: NoteListState,
    innerPadding: PaddingValues,
    navigateToNote: (Long) -> Unit,
    noteToDelete: MutableLongState,
    showDeleteDialog: MutableState<Boolean>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = true
    ) {
        items(state.notes) {note ->
            NoteItemComposable(
                note = note,
                onClickNote = {
                    note.id?.let {navigateToNote(it)}
                },
                onLongClickNote = {
                    showDeleteDialog.value = true
                    noteToDelete.longValue = note.id!!
                }
            )
        }
    }

    if (state.loading) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItemComposable(
    note: Note,
    onClickNote: () -> Unit,
    onLongClickNote: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClickNote,
                onLongClick = onLongClickNote
            )
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
