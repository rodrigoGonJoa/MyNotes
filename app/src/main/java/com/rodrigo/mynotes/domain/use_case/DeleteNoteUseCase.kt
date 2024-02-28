package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.Outcome
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int?): Outcome<Unit> {
        if (noteId == null) {
            return Outcome.Error("El id de la nota es nulo.")
        }
        try {
            noteRepository.deleteNote(noteId)
        } catch (e: Exception) {
            return Outcome.Error("Ha ocurrido un error al eliminar la nota.")
        }
        return Outcome.Success(Unit, "La nota se ha eliminado correctamente.")
    }
}