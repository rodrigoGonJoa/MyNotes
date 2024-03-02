package com.rodrigo.mynotes.presentation.add_edit_note

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val noteId = savedStateHandle.get<Long>("noteId")
    private val _state = MutableStateFlow(AddEditState())
    val state = _state.asStateFlow()

    init {
        getNoteIfExist(noteId)
    }

    fun getNoteIfExist(noteId: Long?) {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.getNoteById(noteId).collectLatest {uiState ->
                _state.update {state ->
                    when (uiState) {
                        is UiState.LoadingState -> state.copy(
                            loading = uiState.loading
                        )

                        is UiState.ErrorState -> state.copy(
                            notificationMessage = uiState.message,
                            successfulAction = false
                        )

                        is UiState.SuccessState -> state.copy(
                            note = uiState.value,
                            notificationMessage = uiState.message,
                            successfulAction = true
                        )
                    }
                }
            }
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.addNote(note).collectLatest {uiState ->
                _state.update {state ->

                    when (uiState) {
                        is UiState.LoadingState -> {

                            state.copy(
                                loading = uiState.loading
                            )
                        }

                        is UiState.ErrorState -> {
                            state.copy(
                                notificationMessage = uiState.message,
                                successfulAction = false
                            )
                        }

                        is UiState.SuccessState -> {
                            state.copy(
                                notificationMessage = uiState.message,
                                successfulAction = true,
                                note = if (uiState.type == StateType.Add) {
                                    note.copy(id = uiState.value as Long)
                                } else {
                                    note
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.deleteNote(note).collectLatest {uiState ->
                _state.update {state ->
                    when (uiState) {
                        is UiState.LoadingState -> state.copy(
                            loading = uiState.loading
                        )

                        is UiState.ErrorState -> state.copy(
                            notificationMessage = uiState.message,
                            successfulAction = false
                        )

                        is UiState.SuccessState -> state.copy(
                            notificationMessage = uiState.message,
                            successfulAction = true,
                            note = Note()
                        )
                    }
                }
            }
        }
    }


    fun saveSomeNotes() {
        val noteList = listOf(
            Note(title = "title1", content = "content1"),
            Note(title = "title2", content = "content2"),
            Note(title = "title3", content = "content3"),
            Note(title = "title4", content = "content4"),
        )
        saveNote(noteList[0])
        saveNote(noteList[1])
        saveNote(noteList[3])
        saveNote(noteList[2])
    }
}