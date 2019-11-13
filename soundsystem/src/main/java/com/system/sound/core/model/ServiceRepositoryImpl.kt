package com.system.sound.core.model

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.system.sound.informations.Audio
import com.system.sound.service.MediaPlayerService
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(private val mContext: Context) :
    ServiceRepository {

    private val TAG = ServiceRepositoryImpl::class.java.simpleName

    private var serviceBound = false
    private lateinit var player: MediaPlayerService

    override fun startService() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isBound(): Boolean = serviceBound

    override fun playAudio(media: Audio) {
        //Check is service is active
        if (!serviceBound) {
            Log.d(TAG, "service is not yet bound, STARTING SERVICE !")
            val playerIntent = Intent(mContext, MediaPlayerService::class.java)
            playerIntent.putExtra("media_path", media.data)
                .putExtra("media_title", media.title)
            mContext.startService(playerIntent)
            //mContext.startService<MediaPlayerService>("media_path" to media.data, "media_title" to media.title) //***************
            mContext.bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            Log.d(TAG, "service already bound   !")
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    override fun onDestroy() {
        mContext.unbindService(serviceConnection)
        player.stopSelf()
    }

    //Binding this Client to the AudioPlayer Service
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.getService()
            serviceBound = true

            Log.d(">>>>>>, ", "Service Bound")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }
}