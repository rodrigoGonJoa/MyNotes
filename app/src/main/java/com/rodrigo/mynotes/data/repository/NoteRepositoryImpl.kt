package com.rodrigo.mynotes.data.repository

import com.rodrigo.mynotes.data.dao.NoteDao
import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.maptoDomain
import com.rodrigo.mynotes.domain.model.InvalidNoteException
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.maptoEntity
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.util.StateType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
): NoteRepository {
    override suspend fun addNote(note: Note): DataState<Long> {
        return try {
            val noteId = noteDao.insertNote(note.maptoEntity())
            if (note.id == noteId || noteId == -1L) {
                throw InvalidNoteException("Ha habido un error al añadir la nota.")
            }
            DataState.SuccessState(
                type = StateType.Added, value = noteId,
                message = "La nota se ha añadido correctamente."
            )
        } catch (ex: Exception) {
            DataState.ErrorState(
                type = StateType.Added, message = ex.message ?: ex.stackTraceToString(), cause = ex
            )
        }
    }

    override suspend fun updateNote(note: Note): DataState<Unit> {
         return try {
            val oldNote = noteDao.getNoteWithCheck(note.maptoEntity().noteId!!)
            val rowsUpdated = noteDao.updateNote(note.maptoEntity())

            if (rowsUpdated != 1) {
                throw InvalidNoteException("Ha habido un error al editar la nota.")
            }
            if (oldNote == note.maptoEntity()) {
                DataState.SuccessState(
                    type = StateType.Updated, value = Unit,
                    message = "No ha habido ningun cambio en la nota."
                )
            }else {
                DataState.SuccessState(
                    type = StateType.Updated, value = Unit,
                    message = "La nota se ha editado correctamente."
                )
            }
        } catch (ex: Exception) {
             DataState.ErrorState(
                type = StateType.Updated, message = ex.message ?: ex.stackTraceToString(),
                cause = ex
            )
        }
    }


    override suspend fun deleteNote(note: Note): DataState<Unit> {
        return try {
            val rowsAffected = noteDao.deleteNote(note.maptoEntity())
            if (rowsAffected != 1) {
                throw InvalidNoteException("Ha habido un error al eliminar la nota.")
            }
            DataState.SuccessState(
                type = StateType.Deleted, value = Unit,
                message = "La nota se ha eliminado correctamente."
            )
        } catch (ex: Exception) {
            DataState.ErrorState(
                type = StateType.Deleted,
                message = ex.message ?: ex.stackTraceToString(), cause = ex
            )
        }
    }

    override suspend fun getNoteById(idNote: Long): DataState<Note> {
        return try {
            val note = noteDao.getNoteWithCheck(idNote).maptoDomain()
            DataState.SuccessState(
                type = StateType.Obtained, value = note,
                message = "La nota se ha obtenido correctamente."
            )
        } catch (ex: Exception) {
            DataState.ErrorState(
                type = StateType.Obtained,
                message = ex.message ?: ex.stackTraceToString(), cause = ex
            )
        }
    }

    // No esta claro que ocurre cuando se bifurca un flujo, suponiendo que es este caso.
    // Probar en la aplicion y entualmente buscar mas informaciona acerca de esto
    override fun getNotes(): Flow<DataState<List<Note>>> {
        val noteEntityFlow = noteDao.getNotes()
        return try {
            noteEntityFlow.map {noteEntityList ->
                DataState.SuccessState(
                    type = StateType.Obtained, value = noteEntityList.map {noteEntity ->
                        noteEntity.maptoDomain()
                    },
                    message = "La lista de notas se ha obtenido correctamente"
                )
            }
        } catch (ex: Exception) {
            noteEntityFlow.map {_ ->
                DataState.ErrorState(
                    type = StateType.Obtained,
                    message = ex.message ?: ex.stackTraceToString(), cause = ex
                )
            }
        }
    }

    companion object {
        const val TAG = "NoteRepositoryImpl"
    }
}