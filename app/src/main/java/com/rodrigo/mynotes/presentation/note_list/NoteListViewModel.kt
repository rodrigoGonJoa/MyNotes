package com.rodrigo.mynotes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.di.IoDispatcher
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _state = MutableStateFlow(NoteListState())
    val state = _state.asStateFlow()

    init {
        getNotes()
    }

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.deleteNote -> {
                deleteNote(event.idNote)
            }
        }
    }

    fun getNotes() {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.getNotes().collectLatest {uiState ->
                _state.update {state ->
                    when (uiState) {
                        is UiState.LoadingState ->
                            state.copy(
                                loading = uiState.loading
                            )

                        is UiState.ErrorState ->
                            state.copy(
                                successfulAction = false
                            )

                        is UiState.SuccessState ->
                            state.copy(
                                notes = uiState.value,
                                successfulAction = true
                            )
                    }
                }
            }
        }
    }

    fun deleteNote(idNote: Long?) {
        viewModelScope.launch(context = dispatcher) {
            noteUseCases.deleteNote(idNote).collectLatest {uiState ->
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
    companion object {
        const val LOG = "notelistviewmodel"
    }
}