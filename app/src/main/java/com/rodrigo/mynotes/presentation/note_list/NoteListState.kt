package com.rodrigo.mynotes.presentation.note_list

import com.rodrigo.mynotes.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val notificationMessage: String? = null,
    val loading: Boolean = false,
    val successfulAction: Boolean = false
)