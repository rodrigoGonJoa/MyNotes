package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.toUiState
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(note: Note?): Flow<UiState<Unit>> {
        return flow {
            emit(UiState.LoadingState(true))
            if (handleDomainRules(note = note, flowCollector = this)) {
                handleRepositoryResult(note = note, flowCollector = this)
            }
            emit(UiState.LoadingState(false))
        }
    }

    private suspend fun handleDomainRules(
        note: Note?,
        flowCollector: FlowCollector<UiState<Unit>>
    ): Boolean {
        if (note == null) {
            flowCollector.emit(UiState.ErrorState("El id de la nota es nulo."))
            return false
        }
        return true
    }

    private suspend fun handleRepositoryResult(
        note: Note?,
        flowCollector: FlowCollector<UiState<Unit>>
    ) {
        note?.let {
            when (val result = noteRepository.deleteNote(note)) {
                is DataState.ErrorState -> flowCollector.emit(result.toUiState())
                is DataState.SuccessState -> flowCollector.emit(result.toUiState())
            }
        }
    }
}