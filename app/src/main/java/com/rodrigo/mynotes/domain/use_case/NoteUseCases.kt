package com.rodrigo.mynotes.domain.use_case

data class NoteUseCases(
    val addNote: AddNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getNoteById: GetNoteByIdUseCase,
    val getNotes: GetNotesUseCase
)

//TODO: terminar de implemetar la inyeccion de dependencias
// establecer el flujo de datos desde base de datso hasta el viewmodel de Note List