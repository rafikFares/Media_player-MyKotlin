package com.system.sound.base

interface Presenter<V : MvpView> {

    fun attachView(view: V)

    fun detachView()

}