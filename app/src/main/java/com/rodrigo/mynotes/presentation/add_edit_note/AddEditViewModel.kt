package com.rodrigo.mynotes.presentation.add_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import com.rodrigo.mynotes.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val noteId = savedStateHandle.get<Long>("noteId")
    private val _state = MutableStateFlow(AddEditState())
    val state = _state.asStateFlow()

    init {
        getNoteIfExist(noteId)
    }

    private fun getNoteIfExist(noteId: Long?) {
        viewModelScope.launch {
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

    fun saveNote() {
        viewModelScope.launch {
            noteUseCases.addNote(_state.value.note).collectLatest {uiState ->
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
                            successfulAction = true
                        )
                    }
                }
            }
        }
    }

    fun deleteNote(noteId: Long?) {
        viewModelScope.launch {
            noteUseCases.deleteNote(noteId).collectLatest {uiState ->
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
                            successfulAction = true
                        )
                    }
                }
            }
        }
    }
}