package com.rodrigo.mynotes.domain.use_case

import android.util.Log
import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.toUiState
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.UiState
import com.rodrigo.mynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(note: Note): Flow<UiState<Any>> {
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
        flowCollector: FlowCollector<UiState<Any>>
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
        flowCollector: FlowCollector<UiState<Any>>
    ) {
        val result = if (note.id == null) {
            noteRepository.addNote(note)
        } else {
            noteRepository.updateNote(note)
        }

        when (result) {
            is DataState.ErrorState -> flowCollector.emit(result.toUiState())

            is DataState.SuccessState -> {
                Log.d("addnoteUseCase", result.type.toString())
                flowCollector.emit(result.toUiState())
            }
        }
    }
}