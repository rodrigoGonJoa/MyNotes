package com.rodrigo.mynotes.presentation.add_edit_note

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodrigo.mynotes.presentation.composables.CustomBasicTextField
import com.rodrigo.mynotes.presentation.composables.hideSoftKeyboardOnClick


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel = hiltViewModel(),
    onClickBack: () -> Unit
) {
    val firstRecomposition = remember {true}
    val contentState = viewModel.contentState
    val titleState = viewModel.titleState
    val loading = viewModel.loading.collectAsState()
    val notificationMessage = viewModel.notificationMessage.collectAsState()
    val actionPerformed = viewModel.actionPerformed.collectAsState()
    val successfulAction = viewModel.successfulAction.collectAsState()
    val snackbarHostState = remember {SnackbarHostState()}


    LaunchedEffect(actionPerformed.value) {
        snackbarHostState.showSnackbar(notificationMessage.value)
    }

    LaunchedEffect(firstRecomposition) {
        viewModel.onEvent(AddEditEvents.GetNote)
    }

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .hideSoftKeyboardOnClick(),
        topBar = {
            ScaffoldTopBar(
                isLoading = loading.value,
                onAdd = {viewModel.onEvent(AddEditEvents.AddNote)},
                onDelete = {viewModel.onEvent(AddEditEvents.DeleteNote)},
                onClickBack = onClickBack,
                successfulAction = successfulAction.value
            )
        },
        snackbarHost = {SnackbarHost(snackbarHostState)}
    ) {innerPadding ->
        ScaffoldContent(
            innerPadding = innerPadding,
            titleState = titleState,
            contentState = contentState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTopBar(
    isLoading: Boolean,
    onDelete: () -> Unit,
    onAdd: () -> Unit,
    onClickBack: () -> Unit,
    successfulAction: Boolean
) {
    TopAppBar(
        navigationIcon = {
            NavigationIcon(onClick = onClickBack)
        },
        actions = {
            ActionsIcon(
                onDelete = onDelete,
                onAdd = onAdd,
                isLoading = isLoading,
                successfulAction = successfulAction
            )
        },
        title = {
            Text("Note", color = Color.White)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
    )
}


@Composable
fun NavigationIcon(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}

@Composable
fun NoteStatus(
    isLoading: Boolean,
    successfulAction: Boolean
) {
    if (isLoading) {
        AnimatedVisibility(visible = true) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        }
    }
//    else if (successfulAction) {
//        Icon(
//            modifier = Modifier.size(30.dp),
//            imageVector = Icons.Default.Check, contentDescription = "Right",
//            tint = Color.White
//        )
//    } else {
//        Icon(
//            modifier = Modifier.size(30.dp),
//            imageVector = Icons.Default.Clear, contentDescription = "Wrong",
//            tint = Color.White
//        )
//    }
}

@Composable
fun ActionsIcon(
    onDelete: () -> Unit,
    onAdd: () -> Unit,
    isLoading: Boolean,
    successfulAction: Boolean
) {
    NoteStatus(isLoading, successfulAction)
    Spacer(modifier = Modifier.width(16.dp))
    IconButton(
        onClick = onDelete,
        enabled = !isLoading
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White
        )
    }
    IconButton(
        onClick = onAdd,
        enabled = !isLoading
    ) {
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
fun TitleTextField(
    titleState: TextFieldState
) {
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
        textFieldColors = ExposedDropdownMenuDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        )
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContentTextField(
    contentState: TextFieldState
) {
    CustomBasicTextField(
        addModifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .border(width = 1.dp, color = Color.Red)
            .padding(8.dp),
        textFieldState = contentState,
        textStyle = TextStyle(color = Color.White),
        cursorColor = Color.White,
        textFieldColors = ExposedDropdownMenuDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
        )
    )
}