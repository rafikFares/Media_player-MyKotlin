package com.system.sound.injection.module

import android.content.Context
import com.system.sound.core.MediaPresenter
import com.system.sound.injection.annotation.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MediaPresenterModule {

    @Provides
    @Singleton
    fun provideMediaPresenter(@ApplicationContext context: Context): MediaPresenter =
        MediaPresenter(context)
}