package com.system.sound.injection.module

import android.app.Application
import android.content.Context
import com.system.sound.injection.annotation.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @ApplicationContext
    @Singleton
    fun provideContext(): Context = app

}