package com.system.sound.base

open class BasePresenter<V : MvpView> : Presenter<V> {

    var myView: V? = null
        private set //setter

    override fun detachView() {
        myView = null
    }

    override fun attachView(view: V) {
        myView = view
    }

    fun isViewAttached(): Boolean = myView != null

}