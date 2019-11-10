package com.system.sound.base

interface BasePresenter<V>{

    fun setView(view : V)
    fun onDetachView()
    fun onDestroy()

}