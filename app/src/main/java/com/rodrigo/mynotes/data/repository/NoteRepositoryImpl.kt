package com.rodrigo.mynotes.data.repository

import com.rodrigo.mynotes.data.model.maptoDomain
import com.rodrigo.mynotes.data.source.NoteDatabase
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.maptoEntity
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val db: NoteDatabase
): NoteRepository {
    override suspend fun addNote(note: Note): DataState<Unit> {
        return try {
            val isAdded = db.getNoteDao().upsertNoteWithCheck(note.maptoEntity())
            if (isAdded) {
                DataState.SuccessState(Unit, "La nota se ha añadido correctamente.")
            } else {
                DataState.SuccessState(Unit, "La nota se ha editado correctamente.")
            }
        } catch (ex: Exception) {
            DataState.ErrorState(
                message = ex.message ?: ex.stackTraceToString(), cause = ex
            )
        }
    }

    override suspend fun deleteNote(idNote: Long): DataState<Unit> {
        return try {
            db.getNoteDao().deleteNoteWithCheck(idNote)
            DataState.SuccessState(Unit, "La nota se ha eliminado correctamente.")
        } catch (ex: Exception) {
            DataState.ErrorState(
                message = ex.message ?: ex.stackTraceToString(), cause = ex
            )
        }
    }

    override suspend fun getNoteById(idNote: Long): DataState<Note> {
        return try {
            val note = db.getNoteDao().getNoteWithCheck(idNote).maptoDomain()
            DataState.SuccessState(note, "La nota se ha obtenido correctamente.")
        } catch (ex: Exception) {
            DataState.ErrorState(
                message = ex.message ?: ex.stackTraceToString(), cause = ex
            )
        }
    }


    override fun getNotes(): Flow<List<Note>> {
        TODO("Not yet implemented")
    }
}