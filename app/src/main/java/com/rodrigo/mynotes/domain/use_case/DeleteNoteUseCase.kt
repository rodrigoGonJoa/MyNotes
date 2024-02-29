package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.DataState
import com.rodrigo.mynotes.util.UiState
import com.rodrigo.mynotes.util.toUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteId: Long?): Flow<UiState<Unit>> {
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
        flowCollector: FlowCollector<UiState<Unit>>
    ): Boolean {
        if (noteId == null) {
            flowCollector.emit(UiState.ErrorState("El id de la nota es nulo."))
            return false
        }
        return true
    }

    private suspend fun handleRepositoryResult(
        noteId: Long?,
        flowCollector: FlowCollector<UiState<Unit>>
    ) {
        noteId?.let {
            when (val result = noteRepository.deleteNote(noteId)) {
                is DataState.ErrorState -> flowCollector.emit(result.toUiState())
                is DataState.SuccessState -> flowCollector.emit(result.toUiState())
            }
        }
    }
}