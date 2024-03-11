@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodrigo.mynotes.presentation.composables

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun NoteComposable(
    //viewModel: AddEditViewModel = hiltViewModel()
) {
    //val state = viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {ScaffoldTopBar()}
    ) {innerPadding ->
        ScaffoldContent(innerPadding = innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTopBar() {
    TopAppBar(
        navigationIcon = {
            NavigationIcon(onClick = { /*TODO*/})
        },
        actions = {
            ActionsIcon(
                onDelete = { /*TODO*/},
                onAdd = {/*TODO*/}
            )
        },
        title = {},
        colors = topAppBarColors(containerColor = Color.Black)
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
fun ActionsIcon(onDelete: () -> Unit, onAdd: () -> Unit) {
    IconButton(onClick = onDelete) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White
        )
    }
    IconButton(onClick = onAdd) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun ScaffoldContent(
    innerPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            TitleTextField()
            Spacer(modifier = Modifier.height(20.dp))
            ContentTextField()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTextField() {
    var titleState by remember {mutableStateOf("")}
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .border(width = 1.dp, color = Color.White),
        value = titleState,
        onValueChange = {titleState = it},
        maxLines = 3,
        singleLine = false,
        placeholder = {Text(text = "Title")},
        colors = textFieldColors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentTextField() {
    val contentState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black)
            .border(width = 1.dp, color = Color.Red)
            .padding(16.dp),
        state = contentState,
        textStyle = TextStyle(
            color = Color.White
        ),
        cursorBrush = SolidColor(Color.White),
        keyboardActions = KeyboardActions{}
    )
}