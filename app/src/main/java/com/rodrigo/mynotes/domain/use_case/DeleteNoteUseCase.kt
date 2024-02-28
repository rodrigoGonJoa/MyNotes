package com.rodrigo.mynotes.domain.use_case

import com.rodrigo.mynotes.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int) {
        noteRepository.deleteNote(noteId)
    }
}