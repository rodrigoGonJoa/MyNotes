package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.model.InvalidNoteException
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.ErrorHandler
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()) {
            throw InvalidNoteException(message = "El titulo no puede estar vacío.")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException(message = "El contenido no puede estar vacío.")
        }
        noteRepository.addNote(note)
    }
}