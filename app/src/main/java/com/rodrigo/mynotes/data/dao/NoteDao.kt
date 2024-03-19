package com.rodrigo.mynotes.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rodrigo.mynotes.data.model.NoteEntity
import com.rodrigo.mynotes.data.utils.DataConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT changes()")
    suspend fun getNumberOfRowsAffected(): Int

    @Query("SELECT COUNT(*) FROM ${DataConstants.NOTE_TABLE_NAME}")
    suspend fun getRowsCount(): Int

    @Transaction
    suspend fun rowsCountChange(): Boolean {
        val rowsCountBefore = getRowsCount()
        return rowsCountBefore != getRowsCount()
    }

    @Query(
        """SELECT * FROM ${DataConstants.NOTE_TABLE_NAME}
        WHERE ${DataConstants.NOTE_ID_COLNAME} == :noteId"""
    )
    suspend fun getNoteById(noteId: Long): NoteEntity?

    @Query("DELETE FROM ${DataConstants.NOTE_TABLE_NAME} WHERE ${DataConstants.NOTE_ID_COLNAME} = :noteId")
    suspend fun deleteNote(noteId: Long): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNote(noteEntity: NoteEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(noteEntity: NoteEntity): Int

    @Query("SELECT * FROM ${DataConstants.NOTE_TABLE_NAME}")
    fun getNotes(): Flow<List<NoteEntity>>
}