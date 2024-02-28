package com.rodrigo.mynotes.presentation.add_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import com.rodrigo.mynotes.util.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val noteId = savedStateHandle.get<Int>("noteId")
    private val _state = MutableStateFlow(AddEditState())
    val state = _state.asStateFlow()

    init {
        getNoteIfExist()
    }

    private fun getNoteIfExist() {
        noteId?.let {id ->
            viewModelScope.launch {
                val result = noteUseCases.getNoteById(id)
                _state.update {state ->
                    when (result) {
                        is Outcome.Error -> state.copy(
                            notificationMessage = result.message
                        )

                        is Outcome.Success -> state.copy(
                            note = result.value
                        )
                    }
                }
            }
        }
    }


    fun saveNote() {
        viewModelScope.launch {
            val result = noteUseCases.addNote(_state.value.note)
            _state.update {state ->
                when (result) {
                    is Outcome.Error -> state.copy(
                        notificationMessage = result.message,
                        successfulAction = false
                    )

                    is Outcome.Success -> state.copy(
                        notificationMessage = result.message,
                        successfulAction = true
                    )
                }
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            val result = noteUseCases.deleteNote(_state.value.note.id)
            _state.update {state ->
                when (result) {
                    is Outcome.Error -> state.copy(
                        notificationMessage = result.message,
                        successfulAction = false
                    )

                    is Outcome.Success -> state.copy(
                        notificationMessage = result.message,
                        successfulAction = true
                    )
                }
            }
        }
    }
}