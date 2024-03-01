package com.rodrigo.mynotes.data.repository

import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.maptoDomain
import com.rodrigo.mynotes.data.source.NoteDatabase
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.maptoEntity
import com.rodrigo.mynotes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    //TODO: No esta del todo claro que ocurre cuando se bifurca un flujo, suponiendo que es este caso.
    // Probar en la aplicion y entualmente buscar mas informaciona acerca de esto
    override fun getNotes(): Flow<DataState<List<Note>>> {
        val noteEntityFlow = db.getNoteDao().getNotes()
        return try {
            noteEntityFlow.map {noteEntityList ->
                DataState.SuccessState(noteEntityList.map {noteEntity ->
                    noteEntity.maptoDomain()
                }, "La lista de notas se ha obtenido correctamente")
            }
        } catch (ex: Exception) {
            noteEntityFlow.map {_ ->
                DataState.ErrorState(
                    message = ex.message ?: ex.stackTraceToString(), cause = ex
                )
            }
        }
    }
}