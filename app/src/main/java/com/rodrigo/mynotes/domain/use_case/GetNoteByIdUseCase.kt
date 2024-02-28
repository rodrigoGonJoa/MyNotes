package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.Outcome
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int): Outcome<Note> {
        val note = noteRepository.getNoteById(noteId)

        return if (note == null) {
            Outcome.Error("No se encontró la nota.")
        } else {
            Outcome.Success(note)
        }

    }
}