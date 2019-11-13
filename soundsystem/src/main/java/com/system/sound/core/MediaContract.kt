package com.system.sound.core

import com.system.sound.base.BasePresenter
import com.system.sound.base.BaseView
import com.system.sound.informations.Audio

interface MediaContract {

    interface Presenter : BasePresenter<View> {
        fun onViewCreated()
        fun playAudio(media: Audio)
    }

    interface View : BaseView<Presenter> {
        fun showData(songList : MutableList<Audio>)
    }
}