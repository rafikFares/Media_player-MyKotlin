package com.automotive.mediaplayer.ui.titles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TitleViewModel : ViewModel() {

    private val mText = MutableLiveData<String>().apply {
        value = "This is title Fragment"
    }
    val text: LiveData<String> = mText
}