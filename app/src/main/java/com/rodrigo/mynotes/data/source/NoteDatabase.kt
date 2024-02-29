package com.rodrigo.mynotes.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rodrigo.mynotes.data.dao.NoteDao
import com.rodrigo.mynotes.data.model.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

}