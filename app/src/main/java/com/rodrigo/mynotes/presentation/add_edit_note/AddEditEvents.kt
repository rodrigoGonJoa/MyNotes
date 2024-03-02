package com.rodrigo.mynotes.presentation.add_edit_note

sealed class AddEditEvents {
    data object DeleteNote: AddEditEvents()
    data object AddNote: AddEditEvents()
    data object GetNote: AddEditEvents()

}