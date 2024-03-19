package com.rodrigo.mynotes.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState


@Composable
fun DeleteNoteDialog(showDialog: MutableState<Boolean>, deleteAction: () -> Unit) {
    AlertDialog(
        onDismissRequest = {showDialog.value = false},
        title = {
            Text(text = "¿Quieres eliminar la nota?")
        },
        dismissButton = {
            Button(
                onClick = {
                    showDialog.value = false
                }
            ) {
                Text(text = "Cancelar")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    showDialog.value = false
                    deleteAction()
                }
            ) {
                Text(text = "Confirmar")
            }
        })
}
