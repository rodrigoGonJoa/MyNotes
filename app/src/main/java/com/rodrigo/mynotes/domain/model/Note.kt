package com.rodrigo.mynotes.domain.model

import com.rodrigo.mynotes.data.model.NoteEntity

data class Note(
    val id: Long = -1L,
    val title: String = "",
    val content: String = ""
)
fun Note.maptoEntity(): NoteEntity{
    return NoteEntity(noteId = id, noteTitle = title, noteContent = content)
}