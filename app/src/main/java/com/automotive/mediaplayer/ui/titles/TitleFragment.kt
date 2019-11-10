package com.automotive.mediaplayer.ui.titles

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.automotive.mediaplayer.MediaViewModel
import com.automotive.mediaplayer.R
import com.automotive.mediaplayer.addNotExistingItems
import com.automotive.mediaplayer.injection.DaggerActivityComponent
import com.system.sound.informations.Audio
import com.automotive.mediaplayer.ui.adapters.RecyclerViewAdapter
import com.system.sound.core.MediaContract
import com.system.sound.core.MediaPresenter
import com.system.sound.injection.AppComponent
import com.system.sound.injection.MediaServiceApplication
import javax.inject.Inject


class TitleFragment : Fragment(), RecyclerViewAdapter.ItemClickListener , MediaContract.View {


    private lateinit var mRecyclerView: RecyclerView
    private var myAudioList: MutableList<Audio> = mutableListOf()

    private val mediaViewModel: MediaViewModel by viewModels()
    private lateinit var mAdapter: RecyclerViewAdapter

    @Inject
    lateinit var mMediaPresenter: MediaPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_title, container, false)

        mRecyclerView = view.findViewById(R.id.rv_title_list)

        mRecyclerView.layoutManager = LinearLayoutManager(context)

        mAdapter = RecyclerViewAdapter(context, myAudioList)
            .setClickListener(this@TitleFragment)

        mRecyclerView.adapter = mAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaViewModel.audioListSize.observe(viewLifecycleOwner, Observer {
            myAudioList.addNotExistingItems(mediaViewModel.audioList.value!!)
            d(">>>>>>>", "audioList changed : $it")
            mAdapter.notifyDataSetChanged()
        })

        mMediaPresenter.onViewCreated()

        d(">>>>>>>", "onViewCreated: ${myAudioList.size}")
    }

    override fun onItemClick(view: View?, position: Int) {
        d(">>>>>>>", "onItemClick: " + mAdapter.songs?.get(position))
        mAdapter.songs?.get(position)?.let { mMediaPresenter.playAudio(it) }
    }

    override fun showData(songList: MutableList<Audio>) {
        mediaViewModel.audioList.value!!.addNotExistingItems(songList)
        mediaViewModel.updateAudioListSize()
        d(">>>>>>>>", "show data : >>>>>> size " + songList.size)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent: AppComponent =(activity?.application as MediaServiceApplication).appComponent

        DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)

        mMediaPresenter.setView(this)
    }

    override fun onDetach() {
        mMediaPresenter.onDetachView()
        super.onDetach()
    }

    override fun onDestroy() {
        mMediaPresenter.onDestroy()
        super.onDestroy()
    }

    override fun setPresenter(presenter: MediaContract.Presenter) {
        // No need to use it, because i already inject the presenter using dagger
    }
}

