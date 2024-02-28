package com.rodrigo.mynotes.domain.use_case

data class NoteUseCases(
    val addNote: AddNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val getNotes: GetNotesUseCase
)