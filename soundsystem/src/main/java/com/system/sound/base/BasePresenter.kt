package com.system.sound.base

interface BasePresenter<V>{

/**
	if i wanted to store the var view in here, i will need to change interface BasePresenter 
	to abstract class BasePresenter to make it hold the variable view 

	protected var view : V? = null

    fun setView(view : V){ this.view = view }
    fun onDetachView(){ this.view = null }
*/

	fun setView(view : V)
    fun onDetachView()
    fun onDestroy()

}