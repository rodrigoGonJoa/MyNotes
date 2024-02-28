package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<Outcome<List<Note>>> {
        val notes = noteRepository.getNotes()
        return notes.mapNotNull { noteList ->
            if (noteList.isNotEmpty()) {
                Outcome.Success(noteList)
            } else {
                Outcome.Error("La lista de notas está vacía.")
            }
        }
    }
}