package com.system.sound.core

import com.system.sound.base.MvpView
import com.system.sound.informations.Audio

interface MediaView : MvpView {

    fun showData(songList : MutableList<Audio>)

}