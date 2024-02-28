package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.Outcome
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int): Outcome<Note> {
        try {
            val note = noteRepository.getNoteById(noteId)
            if (note == null) {
                return Outcome.Error("No se encontró la nota.")
            }
            return Outcome.Success(note, "")
        } catch (e: Exception) {
            return Outcome.Error("Ha ocurrido un error al obtener la nota.")
        }
    }
}