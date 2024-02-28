package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.Outcome
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Outcome<Unit> {
        if (note.title.isBlank()) {
            return Outcome.Error(message = "El titulo no puede estar vacío.")
        }
        if (note.content.isBlank()) {
            return Outcome.Error(message = "El contenido no puede estar vacío.")
        }
        noteRepository.addNote(note)
        return Outcome.Success(Unit)
    }
}