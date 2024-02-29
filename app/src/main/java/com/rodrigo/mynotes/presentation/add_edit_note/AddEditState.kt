package com.rodrigo.mynotes.presentation.add_edit_note

import com.rodrigo.mynotes.domain.model.Note

data class AddEditState(
    val note: Note = Note(),
    val successfulAction: Boolean = false,
    val notificationMessage: String = "",
    val loading: Boolean = true
)