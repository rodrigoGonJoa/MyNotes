package com.rodrigo.mynotes.presentation.add_edit_note

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.di.IoDispatcher
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import com.rodrigo.mynotes.utils.StateType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@OptIn(ExperimentalFoundationApi::class)
class AddEditViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val noteIdfromNoteList = savedStateHandle.get<Long>("noteId")

    val titleState = TextFieldState(initialText = "")
    val contentState = TextFieldState(initialText = "")

    private val _noteId = MutableStateFlow<Long?>(value = null)

    private val _notificationMessage = MutableStateFlow(value = "")
    val notificationMessage = _notificationMessage.asStateFlow()

    private val _actionPerformed = MutableStateFlow(value = false)
    val actionPerformed = _actionPerformed.asStateFlow()

    private val _successfulAction = MutableStateFlow(value = true)
    val successfulAction = _successfulAction.asStateFlow()

    private val _loading = MutableStateFlow(value = true)
    val loading = _loading.asStateFlow()

    fun onEvent(event: AddEditEvents){
        when(event){
            AddEditEvents.AddNote -> {
                saveNote()
                _actionPerformed.value = !_actionPerformed.value
            }
            AddEditEvents.DeleteNote -> {
                deleteNote(_noteId.value)
                _actionPerformed.value = !_actionPerformed.value
            }
            AddEditEvents.GetNote -> {
                getNoteIfExist(6)
            }
        }
    }

    private fun createNote(): Note {
        return Note(
            id = _noteId.value,
            title = titleState.text.toString(),
            content = contentState.text.toString()
        )
    }

    private fun setNoteState(note: Note) {
        _noteId.update {note.id}
        titleState.edit {replace(0, length, note.title)}
        contentState.edit {replace(0, length, note.content)}
    }

    private fun getNoteIfExist(noteId: Long?) {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.getNoteById(noteId).collectLatest {uiState ->
                when (uiState) {
                    is UiState.LoadingState -> _loading.update {uiState.loading}

                    is UiState.ErrorState -> {
                        _notificationMessage.update {uiState.message}
                        _successfulAction.update {false}
                    }

                    is UiState.SuccessState -> {
                        setNoteState(uiState.value)
                        _notificationMessage.update {uiState.message}
                        _successfulAction.update {true}
                    }
                }
            }
        }
    }

    private fun saveNote() {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.addNote(createNote()).collectLatest {uiState ->
                when (uiState) {
                    is UiState.LoadingState -> _loading.update {uiState.loading}

                    is UiState.ErrorState -> {
                        _notificationMessage.update {uiState.message}
                        _successfulAction.update {false}
                    }

                    is UiState.SuccessState -> {
                        _notificationMessage.update {uiState.message}
                        _successfulAction.update {true}
                        if (uiState.type == StateType.Add) {
                            _noteId.update {uiState.value as Long}
                            Log.d("SuccessState", uiState.value.toString())
                        }
                    }
                }
            }
        }
    }


    private fun deleteNote(noteId: Long?) {
        viewModelScope.launch(context = dispatcher) {
            noteId?.let {id ->
                noteUseCases.deleteNote(id).collectLatest {uiState ->
                    when (uiState) {
                        is UiState.LoadingState -> _loading.update {uiState.loading}

                        is UiState.ErrorState -> {
                            _notificationMessage.update {uiState.message}
                            _successfulAction.update {false}
                        }

                        is UiState.SuccessState -> {
                            _notificationMessage.update {uiState.message}
                            _successfulAction.update {true}
                            setNoteState(Note())
                        }
                    }
                }
            }
        }
    }
}