package com.rodrigo.mynotes.presentation.note_list

sealed class NoteListEvent {
    data class deleteNote(val idNote: Long?): NoteListEvent()
}