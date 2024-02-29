package com.rodrigo.mynotes.presentation.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import com.rodrigo.mynotes.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            noteUseCases.getNotes().collect {result ->
                _state.update {state ->
                    when (result) {
                        is DataState.ErrorState -> state.copy(
                            notificationMessage = result.message
                        )

                        is DataState.SuccessState -> state.copy(
                            notes = result.value
                        )
                    }
                }
            }
        }
    }
}