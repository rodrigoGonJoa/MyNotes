package com.rodrigo.mynotes.presentation.add_edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodrigo.mynotes.domain.model.Note

@Composable
fun AddEditScreen(
    viewmodel: AddEditViewModel = hiltViewModel()
) {
    val state by viewmodel.state.collectAsState()
    val message = state.notificationMessage
    val note = state.note

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {viewmodel.saveSomeNotes()}) {
            Text(text = "Save some notes")
        }
        Button(onClick = {
            viewmodel.saveNote(
                Note(25,title = "s6", content = "se ha modificado")
            )
        }) {
            Text(text = "Save a note")
        }
        Button(onClick = {viewmodel.getNoteIfExist(state.note.id)}) {
            Text(text = "Get a note")
        }
        Text(text = message)
        Text(text = note.toString())
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

    //todo: buscar informacion acerca de cuando usar constantes y como administarlas a lo largo del proyecto si
    // con ficheros en cada capa o usar constantes globales lomenos posibles o usar cosntantes en los propios
    // ficheros donde se usen.


}