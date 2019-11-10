package com.system.sound.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log.d
import com.system.sound.informations.Audio
import com.system.sound.service.MediaPlayerService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaPresenter @Inject constructor(private val mContext: Context) : MediaContract.Presenter {

    private val TAG = MediaPresenter::class.java.simpleName
    private var serviceBound = false
    private lateinit var player: MediaPlayerService
    private lateinit var request: Job
    private var view: MediaContract.View? = null

    override fun setView(view: MediaContract.View) {
        this.view = view
    }

    override fun onViewCreated() {
        request = GlobalScope.launch {
            //could use doAsync {}
            view?.showData(
                MediaModel.loadAudioFiles(mContext)
            )
        }
    }

    override fun onDetachView() {
        if (request.isActive)
            request.cancel()

        this.view = null
    }

    override fun onDestroy() {
        if (serviceBound) {
            mContext.unbindService(serviceConnection)
            player.stopSelf()
        }
    }

    fun playAudio(media: Audio) {
        //Check is service is active
        if (!serviceBound) {
            d(TAG, "service is not yet bound, STARTING SERVICE !")
            val playerIntent = Intent(mContext, MediaPlayerService::class.java)
            playerIntent.putExtra("media_path", media.data)
                .putExtra("media_title", media.title)
            mContext.startService(playerIntent)
            //mContext.startService<MediaPlayerService>("media_path" to media.data, "media_title" to media.title) //***************
            mContext.bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            d(TAG, "service already bound   !")
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    //Binding this Client to the AudioPlayer Service
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.getService()
            serviceBound = true

            d(">>>>>>, ", "Service Bound")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }


}