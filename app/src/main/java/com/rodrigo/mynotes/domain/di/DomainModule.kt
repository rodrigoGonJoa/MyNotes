package com.rodrigo.mynotes.domain.di

import com.rodrigo.mynotes.data.dao.NoteDao
import com.rodrigo.mynotes.data.repository.NoteRepositoryImpl
import com.rodrigo.mynotes.data.repository.TransactionProvider
import com.rodrigo.mynotes.data.source.NoteDatabase
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.domain.use_case.AddNoteUseCase
import com.rodrigo.mynotes.domain.use_case.DeleteNoteUseCase
import com.rodrigo.mynotes.domain.use_case.GetNoteByIdUseCase
import com.rodrigo.mynotes.domain.use_case.GetNotesUseCase
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun providesNoteRepository(noteDao: NoteDao, transactionProvider: TransactionProvider): NoteRepository {
        return NoteRepositoryImpl(noteDao, transactionProvider)
    }

    @Provides
    @Singleton
    fun providesTransactionProvider(database: NoteDatabase): TransactionProvider {
        return TransactionProvider(database)
    }

    @Provides
    @Singleton
    fun providesUseCases(noteRepository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            addNote = AddNoteUseCase(noteRepository),
            deleteNote = DeleteNoteUseCase(noteRepository),
            getNoteById = GetNoteByIdUseCase(noteRepository),
            getNotes = GetNotesUseCase(noteRepository)
        )
    }
}