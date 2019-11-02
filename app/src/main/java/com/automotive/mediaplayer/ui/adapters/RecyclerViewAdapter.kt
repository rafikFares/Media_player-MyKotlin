package com.automotive.mediaplayer.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.automotive.mediaplayer.R
import com.system.sound.informations.Audio

class RecyclerViewAdapter(private val context: Context?, val songs: MutableList<Audio>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private var mClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.title_item, parent, false))


    override fun getItemCount(): Int = songs?.size ?: 0


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTvSongTitle.text = songs?.get(position)?.title ?: ""
        holder.mTvSongAlbum.text = songs?.get(position)?.album ?: ""
        //holder.mIvSongPic.setImageBitmap()
    }

    fun setClickListener(itemClickListener: ItemClickListener) : RecyclerViewAdapter {
        mClickListener = itemClickListener
        return this
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var mTvSongTitle: TextView = view.findViewById(R.id.tv_song_title)
        var mTvSongAlbum: TextView = view.findViewById(R.id.tv_song_album)
        var mIvSongPic: ImageView = view.findViewById(R.id.iv_picture)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mClickListener?.onItemClick(v, adapterPosition)
        }

    }
}