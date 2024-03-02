package com.rodrigo.mynotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rodrigo.mynotes.data.model.NoteEntity
import com.rodrigo.mynotes.domain.model.InvalidNoteException
import com.rodrigo.mynotes.util.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT changes()")
    suspend fun getNumberOfRowsAffected(): Int

    @Query("SELECT COUNT(*) FROM ${Constants.NOTE_TABLE_NAME}")
    suspend fun getRowsCount(): Int

    @Transaction
    suspend fun rowsCountChange(): Boolean {
        val rowsCountBefore = getRowsCount()
        return rowsCountBefore != getRowsCount()
    }

    @Query(
        """SELECT * FROM ${Constants.NOTE_TABLE_NAME}
        WHERE ${Constants.NOTE_ID_COLNAME} == :noteId"""
    )
    suspend fun getNoteById(noteId: Long): NoteEntity?


    @Transaction
    suspend fun getNoteWithCheck(noteId: Long): NoteEntity {
        val newNote: NoteEntity? = getNoteById(noteId)
        val rowsAffected = getNumberOfRowsAffected()
        if (newNote == null) {
            throw NullPointerException("La nota obtenida es nula.")
        }
        if (newNote.noteId != noteId || rowsAffected != 1) {
            throw InvalidNoteException("Ha habido un error al obtener la nota.")
        }
        return newNote
    }

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNote(noteEntity: NoteEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(noteEntity: NoteEntity): Int

    @Query("SELECT * FROM ${Constants.NOTE_TABLE_NAME}")
    fun getNotes(): Flow<List<NoteEntity>>
}