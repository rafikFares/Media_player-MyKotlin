package com.automotive.mediaplayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.system.sound.informations.Audio

class MediaViewModel : ViewModel() {

    private val mAudioList = MutableLiveData<MutableList<Audio>>().apply {
        value = mutableListOf<Audio>()
    }

    val audioList: LiveData<MutableList<Audio>> = mAudioList


    val audioListSize: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply { value = audioList.value!!.size }
    }

    fun addAudioToList(audio: Audio){
        Log.d("MediaViewModel", ":>>>>>>>>> addAudioToList add $audio")
        audioList.value!!.add(audio)
    }

    fun updateAudioListSize() {
        audioListSize.postValue(audioList.value!!.size)
    }
}