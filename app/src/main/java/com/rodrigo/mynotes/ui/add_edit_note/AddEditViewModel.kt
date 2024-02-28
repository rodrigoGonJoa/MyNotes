package com.rodrigo.mynotes.ui.add_edit_note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.domain.model.InvalidNoteException
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
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
                noteUseCases.getNoteById(id)?.also {dbNote ->
                    _state.update {
                        it.copy(
                            note = dbNote,
                            loading = false
                        )
                    }
                }
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(_state.value.note)
            } catch (e: InvalidNoteException) {
                _state.update {state ->
                    state.copy(
                        notificationMessage = e.message
                            ?: "AddEditViewModel - saveNote - InvalidNoteException"
                    )
                }
            } catch (e: Exception) {
                _state.update {state ->
                    state.copy(
                        notificationMessage = e.message
                            ?: "AddEditViewModel - saveNote - Exception"
                    )
                }
            }
        }
    }
}