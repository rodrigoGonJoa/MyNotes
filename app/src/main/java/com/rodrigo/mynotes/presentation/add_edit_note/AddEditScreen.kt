package com.rodrigo.mynotes.presentation.add_edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodrigo.mynotes.presentation.composables.CustomBasicTextField
import com.rodrigo.mynotes.presentation.composables.DeleteNoteDialog
import com.rodrigo.mynotes.presentation.composables.hideSoftKeyboardOnClick


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel = hiltViewModel(),
    onClickBack: () -> Unit,
    noteIdFromNoteList: Long? = -1L
) {
    viewModel.setNoteId(noteIdFromNoteList)

    val snackbarHostState = remember {SnackbarHostState()}
    val isFirstComposition = remember {mutableStateOf(true)}
    val actionPerformedToggle = viewModel.actionPerformedToggle.collectAsState()
    val showDeleteDialog = remember {mutableStateOf(false)}

    LaunchedEffect(actionPerformedToggle.value) {
        if (!isFirstComposition.value) {
            snackbarHostState.showSnackbar(viewModel.notificationMessage.value)
        }
    }

    BackHandler {
        viewModel.onEvent(AddEditEvents.OnAddNote).also {onClickBack()}
    }

    LaunchedEffect(remember {true}) {
        viewModel.onEvent(AddEditEvents.OnGetNote)
        isFirstComposition.value = false
    }

    if (showDeleteDialog.value) {
        DeleteNoteDialog(showDialog = showDeleteDialog, deleteAction = {
            viewModel.onEvent(AddEditEvents.OnDeleteNote).also {onClickBack()}
        })
    }

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .hideSoftKeyboardOnClick(),
        topBar = {
            ScaffoldTopBar(
                isLoading = viewModel.loading.collectAsState().value,
                onClickBack = onClickBack,
                showDeleteDialog = showDeleteDialog,
                onAdd = {viewModel.onEvent(AddEditEvents.OnAddNote)}
            )
        },
        snackbarHost = {SnackbarHost(snackbarHostState)}
    ) {innerPadding ->
        ScaffoldContent(
            innerPadding = innerPadding,
            titleState = viewModel.titleState,
            contentState = viewModel.contentState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTopBar(
    isLoading: Boolean,
    onClickBack: () -> Unit,
    onAdd: () -> Unit,
    showDeleteDialog: MutableState<Boolean>
) {
    TopAppBar(
        navigationIcon = {NavigationIcon(onClick = {onAdd().also {onClickBack()}})},
        actions = {
            ActionsIcon(
                isLoading = isLoading,
                showDeleteDialog = showDeleteDialog,
                onAdd = onAdd
            )
        },
        title = {Text("Note", color = Color.White)},
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
    )
}


@Composable
fun NavigationIcon(onClick: () -> Unit) {
    IconButton(modifier = Modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}

@Composable
fun NoteStatus(isLoading: Boolean) {
    if (isLoading) {
        AnimatedVisibility(visible = true) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        }
    }
}

@Composable
fun ActionsIcon(
    isLoading: Boolean,
    onAdd: () -> Unit,
    showDeleteDialog: MutableState<Boolean>
) {
    NoteStatus(isLoading)
    Spacer(modifier = Modifier.width(16.dp))
    IconButton(onClick = {showDeleteDialog.value = true}, enabled = !isLoading) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White
        )
    }
    IconButton(onClick = onAdd, enabled = !isLoading) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScaffoldContent(
    innerPadding: PaddingValues,
    titleState: TextFieldState,
    contentState: TextFieldState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.Black)
            .hideSoftKeyboardOnClick()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .hideSoftKeyboardOnClick()
        )
        TitleTextField(titleState)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .hideSoftKeyboardOnClick()
        )
        ContentTextField(contentState)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TitleTextField(titleState: TextFieldState) {
    CustomBasicTextField(
        addModifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .border(width = 1.dp, color = Color.White)
            .padding(8.dp),
        textFieldState = titleState,
        cursorColor = Color.White,
        textStyle = TextStyle(color = Color.White),
        placeholder = "Title",
        textFieldColors = textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        )
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContentTextField(contentState: TextFieldState) {
    CustomBasicTextField(
        addModifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .border(width = 1.dp, color = Color.Red)
            .padding(8.dp),
        textFieldState = contentState,
        textStyle = TextStyle(color = Color.White),
        cursorColor = Color.White,
        textFieldColors = textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        )
    )
}
