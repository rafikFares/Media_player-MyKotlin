package com.automotive.mediaplayer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val mText = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val mBound = MutableLiveData<Boolean>().apply {
        value = false
    }
    val text: LiveData<String> = mText
    val bound: LiveData<Boolean> = mBound
}