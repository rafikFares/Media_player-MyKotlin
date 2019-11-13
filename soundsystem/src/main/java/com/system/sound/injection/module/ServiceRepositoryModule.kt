package com.system.sound.injection.module

import android.content.Context
import com.system.sound.core.model.ServiceRepository
import com.system.sound.core.model.ServiceRepositoryImpl
import com.system.sound.injection.annotation.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceRepositoryModule {

    @Provides
    @Singleton
    fun provideServiceRepository(@ApplicationContext context: Context): ServiceRepository =
        ServiceRepositoryImpl(context)

}