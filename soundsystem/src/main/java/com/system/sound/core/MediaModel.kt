package com.system.sound.core

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.system.sound.informations.Audio

object MediaModel {

    //@RequiresApi(Build.VERSION_CODES.Q)
    fun loadAudioFiles(context : Context): MutableList<Audio> {
        Log.d(">>>>>>>>>  ", "loading songs ")
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor: Cursor? = context.contentResolver.query(uri, null, selection, null, sortOrder)

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