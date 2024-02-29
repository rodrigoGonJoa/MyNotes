package com.rodrigo.mynotes.di

import com.rodrigo.mynotes.data.dao.NoteDao
import com.rodrigo.mynotes.data.source.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesNoteDao(noteDatabase: NoteDatabase): NoteDao{
        return noteDatabase.getNoteDao()
    }
}