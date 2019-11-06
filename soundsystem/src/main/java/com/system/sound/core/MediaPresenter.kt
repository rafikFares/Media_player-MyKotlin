package com.system.sound.core

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log.d
import androidx.annotation.RequiresApi
import com.system.sound.base.BasePresenter
import com.system.sound.informations.Audio
import com.system.sound.service.MediaPlayerService
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class MediaPresenter @Inject constructor(private val mContext: Context) :
    BasePresenter<MediaView>() {

    private val TAG = MediaPresenter::class.java.simpleName
    private var serviceBound = false
    private lateinit var player: MediaPlayerService

    override fun attachView(view: MediaView) {
        super.attachView(view)
        doAsync {
            view.showData(
                loadAudioFiles()
            )
        }
    }


    override fun detachView() {
        super.detachView()
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

    //@RequiresApi(Build.VERSION_CODES.Q)
    private fun loadAudioFiles(): MutableList<Audio> {
        d(">>>>>>>>>  ", "loading songs ")
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor: Cursor? = mContext.contentResolver.query(uri, null, selection, null, sortOrder)

        val tmp: MutableList<Audio> = mutableListOf()

        if (cursor != null) {
            with(tmp) {
                (1..cursor.count).mapNotNull {
                    cursor.moveToNext()
                    add(
                        Audio(
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.RELATIVE_PATH)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        )
                    )
                }
            }
        }
        cursor?.close()

        return tmp
    }
}