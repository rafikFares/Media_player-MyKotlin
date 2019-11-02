package com.automotive.mediaplayer.injection.module

import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    @Provides
    fun provideString() = "test"
}