package com.rodrigo.mynotes.ui.add_edit_note

import com.rodrigo.mynotes.domain.model.Note

data class AddEditState(
    val note: Note = Note(),
    val noteSaved: Boolean = false,
    val loading: Boolean = true,
    val notificationMessage: String = ""
)