package com.system.sound.core

import com.system.sound.core.model.FilesRepository
import com.system.sound.core.model.ServiceRepository
import com.system.sound.informations.Audio
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaPresenter @Inject constructor(
    private val mServiceRepository: ServiceRepository,
    private val mFilesRepository: FilesRepository
) : MediaContract.Presenter {

    private val TAG = MediaPresenter::class.java.simpleName
    private var view: MediaContract.View? = null
    private lateinit var request: Job


    override fun setView(view: MediaContract.View) {
        this.view = view
    }

    override fun onViewCreated() {
        request = GlobalScope.launch {
            //could use doAsync {}
            view?.showData(
                mFilesRepository.loadAudioFiles()
            )
        }
    }

    override fun onDetachView() {
        if (request.isActive)
            request.cancel()

        this.view = null
    }

    override fun onDestroy() {
        mServiceRepository.onDestroy()
    }

    override fun playAudio(media: Audio) {
        mServiceRepository.playAudio(media)
    }


}