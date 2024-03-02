package com.rodrigo.mynotes.data.repository

import com.rodrigo.mynotes.data.dao.NoteDao
import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.data.model.maptoDomain
import com.rodrigo.mynotes.domain.model.InvalidNoteException
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.domain.model.maptoEntity
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.utils.StateType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val transactionProvider: TransactionProvider
): NoteRepository {
    override suspend fun addNote(note: Note): DataState<Long> {
        return try {
            val noteId = noteDao.insertNote(note.maptoEntity())
            if (note.id == noteId || noteId == -1L) {
                throw InvalidNoteException("Ha habido un error al añadir la nota.")
            }
            DataState.SuccessState(
                type = StateType.Add, value = noteId,
                message = "La nota se ha añadido correctamente."
            )
        } catch (ex: Exception) {
            DataState.ErrorState(
                type = StateType.Add, message = ex.message ?: ex.stackTraceToString(), cause = ex
            )
        }
    }

    override suspend fun updateNote(note: Note): DataState<Unit> {
        return try {
            transactionProvider.runAsTransaction {
                val newNote = noteDao.getNoteById(note.maptoEntity().noteId!!)
                    ?: throw NullPointerException("No se pudo encontrar la nota para actualizar.")

                if (noteDao.updateNote(note.maptoEntity()) != 1) {
                    throw InvalidNoteException("Ha ocurrido un error al actualizar la nota.")
                }

                DataState.SuccessState(
                    type = StateType.Update,
                    value = Unit,
                    message = if (newNote == note.maptoEntity()) {
                        "La nota no ha sido modificada."
                    } else {
                        "La nota se ha actualizado correctamente."
                    }
                )
            }
        } catch (ex: Exception) {
            DataState.ErrorState(
                type = StateType.Update,
                message = ex.message ?: ex.stackTraceToString(),
                cause = ex
            )
        }
    }

    override suspend fun getNoteById(idNote: Long): DataState<Note> {
        return try {
            val note = noteDao.getNoteById(idNote)?.maptoDomain()
                ?: throw NullPointerException("No se pudo encontrar la nota.")

            DataState.SuccessState(
                type = StateType.Obtain, value = note,
                message = "La nota se ha obtenido correctamente."
            )
        } catch (ex: Exception) {
            DataState.ErrorState(
                type = StateType.Obtain,
                message = ex.message ?: ex.stackTraceToString(), cause = ex
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
                type = StateType.Delete, value = Unit,
                message = "La nota se ha eliminado correctamente."
            )
        } catch (ex: Exception) {
            DataState.ErrorState(
                type = StateType.Delete,
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
                    type = StateType.Obtain, value = noteEntityList.map {noteEntity ->
                        noteEntity.maptoDomain()
                    },
                    message = "La lista de notas se ha obtenido correctamente"
                )
            }
        } catch (ex: Exception) {
            noteEntityFlow.map {_ ->
                DataState.ErrorState(
                    type = StateType.Obtain,
                    message = ex.message ?: ex.stackTraceToString(), cause = ex
                )
            }
        }
    }

    companion object {
        const val TAG = "NoteRepositoryImpl"
    }
}