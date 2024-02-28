package com.rodrigo.mynotes.ui.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import com.rodrigo.mynotes.util.Outcome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val notificationMessage: String = ""
)

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
                        is Outcome.Error -> state.copy(
                            notificationMessage = result.message
                                ?: "Ha ocurrido un error al obtener la lista de notas."
                        )

                        is Outcome.Success -> state.copy(
                            notes = result.value
                        )
                    }
                }
            }
        }
    }
}