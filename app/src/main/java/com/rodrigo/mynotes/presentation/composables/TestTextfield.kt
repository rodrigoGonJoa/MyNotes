package com.rodrigo.mynotes.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun TestTextField() {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .safeDrawingPadding()
            .clickable {keyboardController?.hide()}

    ) {
        val contentState = rememberTextFieldState()
        CustomBasicTextField(
            additionalModifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .border(width = 1.dp, color = Color.Red)
                .padding(16.dp),
            textStyle = TextStyle(
                color = Color.White
            ),
            cursorColor = Color.White,
            textFieldState = contentState
        )
    }
}