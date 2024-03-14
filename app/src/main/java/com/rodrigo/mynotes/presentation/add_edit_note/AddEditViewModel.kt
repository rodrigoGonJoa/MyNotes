package com.rodrigo.mynotes.presentation.add_edit_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldState
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
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    val titleState = TextFieldState(initialText = "")
    val contentState = TextFieldState(initialText = "")

    private val _noteId = MutableStateFlow<Long?>(value = null)
    val noteId = _noteId.asStateFlow()
    fun setNoteId(noteId: Long?) {
        if(noteId != -1L) {
            _noteId.update {noteId}
        }
    }

    private val _notificationMessage = MutableStateFlow(value = "")
    val notificationMessage = _notificationMessage.asStateFlow()

    private val _actionPerformedToggle = MutableStateFlow(value = false)
    val actionPerformedToggle = _actionPerformedToggle.asStateFlow()
    private fun changeActionPerformedToggle(){
        _actionPerformedToggle.update {!_actionPerformedToggle.value}
    }

    private val _successfulAction = MutableStateFlow(value = false)
    val successfulAction = _successfulAction.asStateFlow()

    private val _loading = MutableStateFlow(value = true)
    val loading = _loading.asStateFlow()

    fun onEvent(event: AddEditEvents) {
        when (event) {
            AddEditEvents.OnAddNote -> {
                saveNote()
                changeActionPerformedToggle()
            }

            AddEditEvents.OnDeleteNote -> {
                deleteNote()
                changeActionPerformedToggle()
            }

            AddEditEvents.OnGetNote -> {
                getNoteIfExist(_noteId.value)
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
                        }
                    }
                }
            }
        }
    }


    private fun deleteNote() {
        viewModelScope.launch(context = dispatcher) {
            noteId.value?.let {id ->
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