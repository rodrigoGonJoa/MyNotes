package com.rodrigo.mynotes.presentation.note_list

import com.rodrigo.mynotes.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val notificationMessage: String = "",
    val loading: Boolean = true,
    val successfulAction: Boolean = false
)