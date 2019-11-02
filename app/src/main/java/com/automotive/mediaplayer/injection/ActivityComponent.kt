package com.automotive.mediaplayer.injection

import com.automotive.mediaplayer.MainActivity
import com.automotive.mediaplayer.injection.module.ActivityModule
import com.automotive.mediaplayer.ui.titles.TitleFragment
import com.system.sound.injection.AppComponent
import com.system.sound.injection.annotation.FeatureScope
import dagger.Component

@Component(modules = [ActivityModule::class], dependencies  = [AppComponent::class])
@FeatureScope
interface ActivityComponent {

    fun inject (mainActivity: MainActivity)
    fun inject (titleFragment: TitleFragment)

}