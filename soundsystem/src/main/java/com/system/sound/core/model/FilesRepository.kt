package com.system.sound.core.model

import com.system.sound.informations.Audio

interface FilesRepository {

    fun loadAudioFiles() : MutableList<Audio>
}