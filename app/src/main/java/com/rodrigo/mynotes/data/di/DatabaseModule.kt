package com.rodrigo.mynotes.data.di

import android.content.Context
import androidx.room.Room
import com.rodrigo.mynotes.data.dao.NoteDao
import com.rodrigo.mynotes.data.source.NoteDatabase
import com.rodrigo.mynotes.data.utils.DataConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext appContext: Context): NoteDatabase {
        return Room.databaseBuilder(
            context = appContext,
            NoteDatabase::class.java,
            DataConstants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesNoteDao(noteDatabase: NoteDatabase): NoteDao{
        return noteDatabase.getNoteDao()
    }

}