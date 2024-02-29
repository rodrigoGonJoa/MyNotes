package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.DataState
import com.rodrigo.mynotes.util.UiState
import com.rodrigo.mynotes.util.toUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(note: Note): Flow<UiState<Unit>> {
        return flow {
            emit(UiState.LoadingState(true))
            if (handleDomainRules(note = note, flowCollector = this)) {
                handleRepositoryResult(note = note, flowCollector = this)
            }
            emit(UiState.LoadingState(false))
        }
    }

    private suspend fun handleDomainRules(
        note: Note,
        flowCollector: FlowCollector<UiState<Unit>>
    ): Boolean {
        if (note.title.isBlank()) {
            flowCollector.emit(UiState.ErrorState("El titulo de la nota no puede estar vacío."))
            return false
        }
        if (note.content.isBlank()) {
            flowCollector.emit(UiState.ErrorState("El contenido de la nota no puede estar vacío."))
            return false
        }
        return true
    }

    private suspend fun handleRepositoryResult(
        note: Note,
        flowCollector: FlowCollector<UiState<Unit>>
    ) {
        when (val result = noteRepository.addNote(note)) {
            is DataState.ErrorState -> flowCollector.emit(result.toUiState())
            is DataState.SuccessState -> flowCollector.emit(result.toUiState())
        }
    }
}