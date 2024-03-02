package com.rodrigo.mynotes.domain.repository

import com.rodrigo.mynotes.data.model.DataState
import com.rodrigo.mynotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: Note): DataState<Long>
    suspend fun updateNote(note: Note): DataState<Unit>
    suspend fun deleteNote(note: Note): DataState<Unit>
    suspend fun getNoteById(idNote: Long): DataState<Note>
    fun getNotes(): Flow<DataState<List<Note>>>
}