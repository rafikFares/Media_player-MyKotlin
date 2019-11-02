package com.automotive.mediaplayer.ui.titles

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.automotive.mediaplayer.MediaViewModel
import com.automotive.mediaplayer.R
import com.automotive.mediaplayer.addNotExistingItems
import com.automotive.mediaplayer.injection.DaggerActivityComponent
import com.system.sound.informations.Audio
import com.automotive.mediaplayer.ui.adapters.RecyclerViewAdapter
import com.system.sound.core.MediaPresenter
import com.system.sound.core.MediaView
import com.system.sound.injection.AppComponent
import com.system.sound.injection.MediaServiceApplication
import javax.inject.Inject


class TitleFragment : Fragment(), RecyclerViewAdapter.ItemClickListener , MediaView {

    private lateinit var mRecyclerView: RecyclerView
    private var myAudioList: MutableList<Audio> = mutableListOf()

    private lateinit var mediaViewModel: MediaViewModel
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

        mediaViewModel =
            ViewModelProviders.of(activity!!).get(MediaViewModel::class.java)

        mediaViewModel.audioListSize.observe(this, Observer {
            myAudioList.addNotExistingItems(mediaViewModel.audioList.value!!)
            d(">>>>>>>", "audioList changed : $it")
            mAdapter.notifyDataSetChanged()
        })

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
    }

    override fun onResume() {
        super.onResume()
        if (mediaViewModel.audioList.value.isNullOrEmpty()) {
            mMediaPresenter.attachView(this)
        }
    }
}
