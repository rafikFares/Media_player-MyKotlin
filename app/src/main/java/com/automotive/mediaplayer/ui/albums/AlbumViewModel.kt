package com.automotive.mediaplayer.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlbumViewModel : ViewModel() {

    private val mText = MutableLiveData<String>().apply {
        value = "This is album Fragment"
    }
    val text: LiveData<String> = mText
}