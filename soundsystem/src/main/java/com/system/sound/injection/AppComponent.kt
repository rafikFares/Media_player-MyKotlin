package com.system.sound.injection

import android.content.Context
import com.system.sound.core.MediaPresenter
import com.system.sound.injection.annotation.ApplicationContext
import com.system.sound.injection.module.AppModule
import com.system.sound.injection.module.MediaPresenterModule
import com.system.sound.injection.module.NotificationHelperModule
import com.system.sound.notification.NotificationHelper
import com.system.sound.service.MediaPlayerService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, MediaPresenterModule::class, NotificationHelperModule::class])
interface AppComponent {

    fun inject(target: MediaPlayerService)

    @ApplicationContext
    fun provideContext(): Context
    fun provideMediaPresenter(): MediaPresenter
    fun provideNotificationHelper(): NotificationHelper

}