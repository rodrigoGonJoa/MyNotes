package com.rodrigo.mynotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rodrigo.mynotes.data.model.NoteEntity
import com.rodrigo.mynotes.domain.model.InvalidNoteException
import com.rodrigo.mynotes.util.Constants

@Dao
interface NoteDao {

    @Query("SELECT changes()")
    suspend fun getNumberOfRowsAffected(): Int

    @Query("SELECT COUNT(*) FROM ${Constants.NOTE_TABLE_NAME}")
    suspend fun getRowsCount(): Int

    @Query(
        "SELECT * FROM ${Constants.NOTE_TABLE_NAME} " +
                "WHERE ${Constants.NOTE_ID_COLNAME} == :noteId"
    )
    suspend fun getNoteById(noteId: Long): NoteEntity?


    @Transaction
    @Throws(InvalidNoteException::class, NullPointerException::class)
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

    @Delete(NoteEntity::class)
    suspend fun deleteNoteById(noteId: Long): Long

    @Transaction
    @Throws(InvalidNoteException::class)
    suspend fun deleteNoteWithCheck(noteId: Long) {
        val newNoteId: Long = deleteNoteById(noteId)
        val rowsAffected = getNumberOfRowsAffected()
        if (newNoteId != noteId || rowsAffected != 1) {
            throw InvalidNoteException("Ha habido un error al eliminar la nota.")
        }
    }

    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Transaction
    suspend fun upsertNoteWithCheck(note: NoteEntity): Boolean {
        val rowsCount: Int = getRowsCount()
        val noteId: Long = upsertNote(note)
        val rowsAffected = getNumberOfRowsAffected()
        val isAdded = (getRowsCount() == rowsCount + 1)

        if (!isAdded && (note.noteId != noteId || rowsAffected != 1)) {
            throw InvalidNoteException("Ha habido un error al editar la nota.")
        }
        if (isAdded && (note.noteId == noteId || rowsAffected != 1)) {
            throw InvalidNoteException("Ha habido un error al añadir la nota.")
        }
        return isAdded
    }
}