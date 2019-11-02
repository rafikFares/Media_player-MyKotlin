package com.system.sound.injection.module

import android.content.Context
import com.system.sound.injection.annotation.ApplicationContext
import com.system.sound.notification.NotificationHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationHelperModule {

    @Provides
    @Singleton
    fun provideMediaPresenter(@ApplicationContext context: Context): NotificationHelper =
        NotificationHelper(context)

}