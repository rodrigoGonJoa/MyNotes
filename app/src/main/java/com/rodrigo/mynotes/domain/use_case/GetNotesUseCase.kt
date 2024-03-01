package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.toUiState
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<UiState<List<Note>>> {
        return flow {
            emit(UiState.LoadingState(true))
            handleRepositoryResult(flowCollector = this)
            emit(UiState.LoadingState(false))
        }
    }
    private suspend fun handleRepositoryResult(
        flowCollector: FlowCollector<UiState<List<Note>>>
    ) {
        noteRepository.getNotes().map {dataState ->
            when (dataState) {
                is DataState.ErrorState -> flowCollector.emit(dataState.toUiState())
                is DataState.SuccessState -> flowCollector.emit(dataState.toUiState())
            }
        }
    }
}