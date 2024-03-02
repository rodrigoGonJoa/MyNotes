package com.rodrigo.mynotes.presentation.add_edit_note

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodrigo.mynotes.domain.model.Note

@Composable
fun AddEditScreen(
    viewmodel: AddEditViewModel = hiltViewModel()
) {
    val state by viewmodel.state.collectAsState()
    val message = state.notificationMessage
    val note = state.note
    val loadingVisibility = state.loading

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Log.d("loading", loadingVisibility.toString())
        if (loadingVisibility) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {viewmodel.saveSomeNotes()}) {Text(text = "Save some notes")}
            Button(onClick = {viewmodel.saveNote(
                Note(title = "shk6", content = "se ha mod")
            )}) {
                Text(text = "Save a note")
            }
            Button(onClick = {viewmodel.getNoteIfExist(state.note.id)}) {Text(text = "Get a note")}
            Button(onClick = {viewmodel.deleteNote(state.note)}) {Text(text = "Delete a note")}
            Text(text = message)
            Text(text = note.toString())
        }
    }


    // todo: mover los mensajes de exito o error a la capa de ui, viewmodel mediante el
    //  tipo de sucess o el tipo de error. evitar tener informacion harcodeada. cuanto mas profunda
    //  la capa, menos varioabilidad debe haber. crear quizas una sealed class con eventos que luego
    //  en viewmodel se conviertan en mensajes

    //todo: crear un nuevo tipo de estado que sea estado de error y separar los de success con los de error en el fichero de statetype.
    // cambair el nombre a SuccessType y ErrorType

    /*todo: guardar en base de datos el hash
       cuando quiero comproabr si se ha editado como cuando quiero obtner la nota al refrescar por ejemplo,
       obtener el hash, compararlos y entonces ahcer una accion.
       tanto en el caso de editar como en el de obtener laa nota al refrescar, devolver por ahora un
       mensaje de no ha habido ningun cambio. lo que he hecho hasta ahora es mediante la propia nota
       comprobar si la nota en base de datos es igual con un compareTo*/

    // todo: establecer el flujo de datos desde base de datso hasta el viewmodel de Note List


}