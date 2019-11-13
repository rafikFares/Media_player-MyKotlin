package com.system.sound.injection.module

import android.content.Context
import com.system.sound.core.model.FilesRepository
import com.system.sound.core.model.FilesRepositoryImpl
import com.system.sound.injection.annotation.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FilesRepositoryModule {

    @Provides
    @Singleton
    fun provideFilesRepository(@ApplicationContext context: Context): FilesRepository =
        FilesRepositoryImpl(context)

}