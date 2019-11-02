package com.system.sound.service

interface MediaAction {

    fun play()
    fun playUrl(url : String)
    fun stop()
    fun pause()
    fun resume()

}