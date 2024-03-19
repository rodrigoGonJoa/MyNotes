package com.rodrigo.mynotes.di

import android.content.Context
import com.rodrigo.mynotes.MyNoteApp
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