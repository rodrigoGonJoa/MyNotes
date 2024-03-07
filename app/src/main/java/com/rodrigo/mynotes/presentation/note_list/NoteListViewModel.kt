package com.rodrigo.mynotes.presentation.note_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.di.IoDispatcher
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _notes = MutableStateFlow(emptyList<Note>())
    val notes = _notes.asStateFlow()

    private val _notificationMessage = MutableStateFlow("")
    val notificationMessage = _notificationMessage.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _successfulAction = MutableStateFlow(false)
    val successfulAction = _successfulAction.asStateFlow()

    init {
        getNotes()
    }

    fun onEvent(event: NoteListEvent){
        when(event){
            is NoteListEvent.deleteNote -> {
                deleteNote(event.idNote)
            }
        }
    }

    private fun getNotes() {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.getNotes().collectLatest {uiState ->
                when (uiState) {
                    is UiState.LoadingState -> _loading.value = uiState.loading
                    is UiState.ErrorState -> _successfulAction.value = false
                    is UiState.SuccessState -> {
                        _notes.value = uiState.value
                        _successfulAction.value = true
                    }
                }
            }
        }
    }


    fun deleteNote(idNote: Long?) {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.deleteNote(idNote).collectLatest {uiState ->
                when (uiState) {
                    is UiState.LoadingState -> _loading.value = uiState.loading
                    is UiState.ErrorState -> {
                        _notificationMessage.value = uiState.message
                        _successfulAction.value = false
                    }

                    is UiState.SuccessState -> {
                        _notificationMessage.value = uiState.message
                        _successfulAction.value = true
                    }
                }
            }
        }
    }

    companion object {
        const val LOG = "notelistviewmodel"
    }
}