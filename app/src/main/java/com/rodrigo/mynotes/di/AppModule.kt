package com.rodrigo.mynotes.di

import android.content.Context
import androidx.room.Room
import com.rodrigo.mynotes.MyNoteApp
import com.rodrigo.mynotes.data.dao.NoteDao
import com.rodrigo.mynotes.data.repository.NoteRepositoryImpl
import com.rodrigo.mynotes.data.source.NoteDatabase
import com.rodrigo.mynotes.domain.repository.NoteRepository
import com.rodrigo.mynotes.domain.use_case.AddNoteUseCase
import com.rodrigo.mynotes.domain.use_case.DeleteNoteUseCase
import com.rodrigo.mynotes.domain.use_case.GetNoteByIdUseCase
import com.rodrigo.mynotes.domain.use_case.GetNotesUseCase
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import com.rodrigo.mynotes.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MyNoteApp {
        return app as MyNoteApp
    }

    @Provides
    @Singleton
    fun provideContext(application: MyNoteApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext appContext: Context): NoteDatabase {
        return Room.databaseBuilder(
            context = appContext,
            NoteDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
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