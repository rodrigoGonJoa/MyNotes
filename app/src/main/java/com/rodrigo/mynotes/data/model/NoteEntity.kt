package com.rodrigo.mynotes.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.util.Constants

@Entity(tableName = Constants.NOTE_TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Constants.NOTE_ID_COLNAME)
    val noteId: Long = -1,
    @ColumnInfo(name = Constants.NOTE_TITLE_COLNAME)
    val noteTitle: String,
    @ColumnInfo(name = Constants.NOTE_CONTENT_COLNAME)
    val noteContent: String,
)

fun NoteEntity.maptoDomain(): Note{
    return Note(id = noteId, title = noteTitle, content = noteContent)
}