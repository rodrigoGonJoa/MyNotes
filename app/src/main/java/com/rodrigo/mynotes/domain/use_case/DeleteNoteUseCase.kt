package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.toUiState
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(idNote: Long): Flow<UiState<Unit>> {
        return flow {
            emit(UiState.LoadingState(true))
            if(handleDomainRules(noteId = idNote, flowCollector = this)){
                noteRepository.deleteNote(idNote).also {result ->
                    when (result) {
                        is DataState.ErrorState -> emit(result.toUiState())
                        is DataState.SuccessState -> emit(result.toUiState())
                    }
                    emit(UiState.LoadingState(false))
                }
            }

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
}