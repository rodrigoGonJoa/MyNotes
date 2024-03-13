package com.rodrigo.mynotes.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

fun Modifier.hideSoftKeyboardOnClick(): Modifier = composed {
    val keyboardController = LocalSoftwareKeyboardController.current
    clickable(
        indication = null,
        interactionSource = remember {MutableInteractionSource()}
    ) {
        keyboardController?.hide()
    }
}