package com.system.sound.core.model

import com.system.sound.informations.Audio

interface ServiceRepository {

    fun startService()
    fun isBound() : Boolean
    fun playAudio(media: Audio)
    fun onDestroy()
}