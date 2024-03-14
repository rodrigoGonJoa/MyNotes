package com.rodrigo.mynotes.presentation.add_edit_note

sealed class AddEditEvents {
    data object OnDeleteNote: AddEditEvents()
    data object OnAddNote: AddEditEvents()
    data object OnGetNote: AddEditEvents()

}