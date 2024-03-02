package com.rodrigo.mynotes.data.repository

import androidx.room.withTransaction
import com.rodrigo.mynotes.data.source.NoteDatabase
import javax.inject.Inject

class TransactionProvider @Inject constructor(
    private val db: NoteDatabase
) {
    suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}