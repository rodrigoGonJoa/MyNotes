package com.rodrigo.mynotes.domain.model

import com.rodrigo.mynotes.data.model.NoteEntity

data class Note(
    val id: Long? = null,
    val title: String = "",
    val content: String = ""
){
    override fun toString(): String {
        return "Note(id=$id, title='$title', content='$content')"
    }
}

fun Note.maptoEntity(): NoteEntity{
    return NoteEntity(noteId = id, noteTitle = title, noteContent = content)
}