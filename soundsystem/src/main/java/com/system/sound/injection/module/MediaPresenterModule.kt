package com.system.sound.injection.module

import com.system.sound.core.MediaPresenter
import com.system.sound.core.model.FilesRepository
import com.system.sound.core.model.ServiceRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MediaPresenterModule {

    @Provides
    @Singleton
    fun provideMediaPresenter(serviceRepository : ServiceRepository, filesRepository: FilesRepository): MediaPresenter =
        MediaPresenter(serviceRepository, filesRepository)
}