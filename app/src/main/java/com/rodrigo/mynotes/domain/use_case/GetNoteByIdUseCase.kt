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

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteId: Long?): Flow<UiState<Note>> {
        return flow {
            emit(UiState.LoadingState(true))
            if (handleDomainRules(noteId = noteId, flowCollector = this)) {
                handleRepositoryResult(noteId = noteId, flowCollector = this)
            }
            emit(UiState.LoadingState(false))
        }
    }

    private suspend fun handleDomainRules(
        noteId: Long?,
        flowCollector: FlowCollector<UiState<Note>>
    ): Boolean {
        if (noteId == null) {
            flowCollector.emit(UiState.ErrorState("El id de la nota es nulo."))
            return false
        }
        if (noteId == -1L) {
            flowCollector.emit(UiState.ErrorState("No existe una nota con el id $noteId"))
            return false
        }
        return true
    }

    private suspend fun handleRepositoryResult(
        noteId: Long?,
        flowCollector: FlowCollector<UiState<Note>>
    ) {
        when (val result = noteRepository.getNoteById(noteId!!)) {
            is DataState.ErrorState -> flowCollector.emit(result.toUiState())
            is DataState.SuccessState -> flowCollector.emit(result.toUiState())
        }
    }
}