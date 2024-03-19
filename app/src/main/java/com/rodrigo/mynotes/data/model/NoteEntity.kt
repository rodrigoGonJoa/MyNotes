package com.rodrigo.mynotes.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodrigo.mynotes.data.utils.DataConstants
import com.rodrigo.mynotes.domain.model.Note

@Entity(tableName = DataConstants.NOTE_TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataConstants.NOTE_ID_COLNAME)
    val noteId: Long?,
    @ColumnInfo(name = DataConstants.NOTE_TITLE_COLNAME)
    val noteTitle: String,
    @ColumnInfo(name = DataConstants.NOTE_CONTENT_COLNAME)
    val noteContent: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteEntity

        if (noteId != other.noteId) return false
        if (noteTitle != other.noteTitle) return false
        return noteContent == other.noteContent
    }

    override fun hashCode(): Int {
        var result = noteTitle.hashCode()
        result = 31 * result + noteContent.hashCode()
        return result
    }
}

fun NoteEntity.maptoDomain(): Note{
    return Note(id = noteId, title = noteTitle, content = noteContent)
}