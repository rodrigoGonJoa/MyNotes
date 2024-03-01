package com.rodrigo.mynotes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
): ViewModel() {
    private val _state = MutableStateFlow(NoteListState())
    val state = _state.asStateFlow()

    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch {
            noteUseCases.getNotes().collectLatest {uiState ->
                _state.update {state ->
                    when(uiState){
                        is UiState.LoadingState -> state.copy(
                            loading = uiState.loading
                        )
                        is UiState.ErrorState -> state.copy(
                            notificationMessage = uiState.message,
                            successfulAction = false
                        )

                        is UiState.SuccessState -> state.copy(
                            notes = uiState.value,
                            notificationMessage = uiState.message,
                            successfulAction = true
                        )
                    }
                }
            }
        }
    }
}