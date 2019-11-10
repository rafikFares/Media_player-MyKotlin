package com.automotive.mediaplayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.system.sound.informations.Audio
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

class MediaViewModel : ViewModel() {

    private val mAudioList = MutableLiveData<MutableList<Audio>>().apply {
        value = mutableListOf<Audio>()
    }

    val audioList: LiveData<MutableList<Audio>> = mAudioList

    val receiveChannel by lazy { // lazy is only to not initialize this variable if we don't need it
        viewModelScope.produce {
            for (i in 0..Int.MAX_VALUE) {
                send(i)
                delay(1000)
            }
        }
    }


    val audioListSize: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply { value = audioList.value!!.size }
    }

    fun addAudioToList(audio: Audio) {
        Log.d("MediaViewModel", ":>>>>>>>>> addAudioToList add $audio")
        audioList.value!!.add(audio)
    }

    fun updateAudioListSize() {
        audioListSize.postValue(audioList.value!!.size)
    }
}