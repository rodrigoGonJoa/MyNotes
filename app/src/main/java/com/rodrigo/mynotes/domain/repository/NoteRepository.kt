package com.rodrigo.mynotes.domain.repository

import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.util.DataState
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: Note): DataState<Unit>
    suspend fun deleteNote(idNote: Long): DataState<Unit>
    suspend fun getNoteById(idNote: Long): DataState<Note>
    fun getNotes(): Flow<List<Note>>
}