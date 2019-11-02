package com.system.sound.informations

import java.io.Serializable

data class Audio(val data: String?, val title: String?, val album: String?, val artist: String?) : Serializable{

    override fun toString(): String {
        return "Audio(data=$data, title=$title, album=$album, artist=$artist)"
    }
}