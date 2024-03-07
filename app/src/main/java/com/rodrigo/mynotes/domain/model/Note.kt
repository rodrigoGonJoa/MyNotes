package com.rodrigo.mynotes.domain.model

import com.rodrigo.mynotes.data.model.NoteEntity

data class Note(
    val id: Long? = null,
    val title: String = "",
    val content: String = ""
) {

    override fun toString(): String {
        return "Note(id=$id, title='$title', content='$content')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false
        if (title != other.title) return false
        return content == other.content
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }

    companion object {
        val default = Note(id = null, title = "Title", content = "Content")
    }
}

fun Note.maptoEntity(): NoteEntity {
    return NoteEntity(noteId = id, noteTitle = title, noteContent = content)
}