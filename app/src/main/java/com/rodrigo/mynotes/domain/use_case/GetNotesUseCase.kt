package com.rodrigo.mynotes.domain.use_case

import android.util.Log
import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.toUiState
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<UiState<List<Note>>> {
        return flow {
            emit(UiState.LoadingState(true))
            noteRepository.getNotes().collect {dataState ->
                when (dataState) {
                    is DataState.ErrorState -> emit(dataState.toUiState())
                    is DataState.SuccessState -> emit(dataState.toUiState())
                }
                emit(UiState.LoadingState(false))
            }
        }
    }
}