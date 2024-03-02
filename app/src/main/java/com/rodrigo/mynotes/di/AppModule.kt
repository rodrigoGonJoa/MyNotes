package com.rodrigo.mynotes.di

import android.content.Context
import com.rodrigo.mynotes.MyNoteApp
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
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
}