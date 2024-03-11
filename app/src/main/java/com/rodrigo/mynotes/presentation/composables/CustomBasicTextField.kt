package com.rodrigo.mynotes.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomBasicTextField(
    additionalModifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    cursorColor: Color = Color.Black,
    textFieldState: TextFieldState = rememberTextFieldState(),
) {
    val focusRequester = remember {FocusRequester()}
    LaunchedEffect(Unit) {focusRequester.requestFocus()}
    BasicTextField(
        modifier = Modifier
            .then(additionalModifier)
            .focusRequester(focusRequester)
            .clearFocusOnKeyboardDismiss(),
        state = textFieldState,
        textStyle = textStyle,
        cursorBrush = SolidColor(cursorColor)
    )
}

@OptIn(ExperimentalLayoutApi::class)
private fun Modifier.clearFocusOnKeyboardDismiss(
): Modifier = composed {
    var isFocused by remember {mutableStateOf(false)}
    val focusManager = LocalFocusManager.current
    var keyboardAppearedSinceLastFocused by remember {mutableStateOf(false)}
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun CustomBasicTextFieldPreview() {
    CustomBasicTextField()
}