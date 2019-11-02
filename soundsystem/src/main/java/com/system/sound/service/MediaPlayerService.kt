package com.system.sound.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log.d
import com.system.sound.injection.AppComponent
import com.system.sound.injection.DaggerAppComponent
import com.system.sound.injection.MediaServiceApplication
import com.system.sound.injection.module.AppModule
import com.system.sound.notification.NotificationHelper
import com.system.sound.player.MyMediaPlayer
import java.io.IOException
import javax.inject.Inject


class MediaPlayerService : Service(), MediaPlayer.OnCompletionListener,
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
    MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
    AudioManager.OnAudioFocusChangeListener, MediaAction {

    private val TAG = MediaPlayerService::class.java.simpleName

    private val iBinder = LocalBinder()
    private val ACTION_PLAY: String = "com.example.action.PLAY"
    private var audioManager: AudioManager? = null

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val mMediaPlayer: MyMediaPlayer by lazy {
        MyMediaPlayer.setOnCompletion(this)
            .setOnPrepared(this)
            .setOnBufferingUpdate(this)
            .setOnError(this)
            .setOnSeekComplete(this)
            .setOnInfo(this)
    }


    private fun initPlayer(mPathSong: String = "", mSongTitle: String = "") {
        d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> initPlayer")
        notificationHelper.showNotification(title = mSongTitle)

        try {
            mMediaPlayer.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMediaPlayer.initAttributes()
        //mMediaPlayer.setWakeMode(applicationContext, PowerManager.FULL_WAKE_LOCK)

        try {
            mMediaPlayer.setDataSource(mPathSong)
        } catch (e: IOException) {
            e.printStackTrace()
            stopSelf()
        }

        mMediaPlayer.prepareAsync()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        //Invoked when playback of a media source has completed.
        stop()
        //stop the service
        stopSelf()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        //Invoked when the media source is ready for playback.
        play()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        //Invoked when there has been an error during an asynchronous operation.
        when (what) {
            MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ->
                d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra")
            MediaPlayer.MEDIA_ERROR_SERVER_DIED ->
                d("MediaPlayer Error", "MEDIA ERROR SERVER DIED $extra")
            MediaPlayer.MEDIA_ERROR_UNKNOWN ->
                d("MediaPlayer Error", "MEDIA ERROR UNKNOWN $extra")
        }
        return false
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
        TODO("not implemented")  //Invoked indicating the completion of a seek operation.
    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("not implemented") //Invoked to communicate some info.
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        TODO("not implemented")
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    override fun onAudioFocusChange(focusChange: Int) {
        //Invoked when the audio focus of the system is updated.
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                d(
                    "MediaPlayer Error",
                    "AUDIOFOCUS_GAIN The service gained audio focus, so it needs to start playing "
                )
                play()
                mMediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                d(
                    "MediaPlayer Error",
                    "AUDIOFOCUS_LOSS : The service lost audio focus, the user probably moved to playing media on another app, so release the media player. "
                )
                stop()
                mMediaPlayer.release()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                d(
                    "MediaPlayer Error",
                    "AUDIOFOCUS_LOSS_TRANSIENT Focus lost for a short time, pause the MediaPlayer"
                )
                pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                d(
                    "MediaPlayer Error",
                    "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK Lost focus for a short time, probably a notification arrived on the device, lower the playback volume"
                )
                mMediaPlayer.setVolume(0.3f, 0.3f)
            }
        }
        TODO("continue implementation")
    }

    override fun onCreate() {
        super.onCreate()

        (application as MediaServiceApplication).appComponent.inject(this)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var songPath = ""
        var songTitle = ""
        try {
            //An audio file is passed to the service through putExtra();
            songPath = intent.extras?.getString("media_path") ?: ""
            songTitle = intent.extras?.getString("media_title") ?: ""
            d(TAG, "onStartCommand >>>>>>>>>>>>> path to play with intent : $songPath")
        } catch (e: NullPointerException) {
            stopSelf()
        }

        if (!acquireAudioFocus(this))
            stopSelf()

        initPlayer(songPath, songTitle)

        return super.onStartCommand(intent, flags, startId)
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@MediaPlayerService
    }

    override fun onDestroy() {
        releaseAudioFocus(this)
        notificationHelper.hideNotification()
        stop()
        mMediaPlayer.release()
        super.onDestroy()
    }

    override fun play() {
        if (!mMediaPlayer.isPlaying) mMediaPlayer.start()
    }

    override fun playUrl(url: String) {
        if (!mMediaPlayer.isPlaying) {
            mMediaPlayer.setDataSource(url)
            mMediaPlayer.prepareAsync()
            mMediaPlayer.acquireWifiLock(applicationContext)
        }
    }

    override fun stop() {
        if (!mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
            mMediaPlayer.releaseWifiLock()
        }
    }

    override fun pause() {
        if (!mMediaPlayer.isPlaying) mMediaPlayer.pause()
    }

    override fun resume() {
        if (!mMediaPlayer.isPlaying) mMediaPlayer.resume()
    }

    private fun acquireAudioFocus(listener: AudioManager.OnAudioFocusChangeListener): Boolean {
        d(">>>>>>>>", "acquireAudioFocus")
        audioManager ?: run {
            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        return when (audioManager?.requestAudioFocus(
            listener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> true
            else -> false
        }
    }

    private fun releaseAudioFocus(listener: AudioManager.OnAudioFocusChangeListener): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager?.abandonAudioFocus(listener)
    }
}