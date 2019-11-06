package com.system.sound.player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.Build

object MyMediaPlayer : MediaPlayer() {

    private var pausedAt: Int = 0
    private const val LOCK_TAG: String = "MyPlayerLock"
    private var wifiLock: WifiManager.WifiLock? = null

    override fun pause() {
        super.pause()
        pausedAt = this.currentPosition
    }

    fun resume() {
        this.seekTo(pausedAt)
        super.start()
    }

    override fun release() {
        super.release()
        releaseWifiLock()
    }

    fun setOnCompletion(listener: OnCompletionListener): MyMediaPlayer {
        this.setOnCompletionListener(listener)
        return this
    }

    fun setOnError(listener: OnErrorListener): MyMediaPlayer {
        this.setOnErrorListener(listener)
        return this
    }

    fun setOnPrepared(listener: OnPreparedListener): MyMediaPlayer {
        this.setOnPreparedListener(listener)
        return this
    }

    fun setOnBufferingUpdate(listener: OnBufferingUpdateListener): MyMediaPlayer {
        this.setOnBufferingUpdateListener(listener)
        return this
    }

    fun setOnSeekComplete(listener: OnSeekCompleteListener): MyMediaPlayer {
        this.setOnSeekCompleteListener(listener)
        return this
    }

    fun setOnInfo(listener: OnInfoListener): MyMediaPlayer {
        this.setOnInfoListener(listener)
        return this
    }

    fun initAttributes(): MyMediaPlayer {
        ifLolipopDo(
            {
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                this.setAudioAttributes(audioAttributes)
            }
            ,
            { this.setAudioStreamType(AudioManager.STREAM_MUSIC) }
        )
        return this
    }

    fun acquireWifiLock(context: Context) {
        wifiLock ?: run {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiLock =
                wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, LOCK_TAG)
        }

        wifiLock?.acquire()
    }

    fun releaseWifiLock() {
        wifiLock?.release()
    }

    inline fun ifLolipopDo(lolipopFun: () -> Unit, notLolipopFun: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            lolipopFun()
        else
            notLolipopFun()
    }
}