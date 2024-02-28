package com.rodrigo.mynotes.domain.repository

import com.rodrigo.mynotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: Note)
    suspend fun deleteNote(idNote: Int)
    suspend fun getNoteById(idNote: Int): Note?
    fun getNotes(): Flow<List<Note>>
}