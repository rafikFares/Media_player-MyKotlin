package com.automotive.mediaplayer.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import com.automotive.mediaplayer.R
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomBadgeHelper{

    private lateinit var mBottomNavigationView: BottomNavigationView

    fun initialize(bottomNavigationView: BottomNavigationView){
        mBottomNavigationView = bottomNavigationView
    }

    fun showBadge(@IdRes itemId: Int, value: Any, context: Context) {
        removeBadge(itemId)
        val itemView = mBottomNavigationView.findViewById<BottomNavigationItemView>(itemId)
        val badge: View = LayoutInflater.from(context).inflate(R.layout.notification_badge, mBottomNavigationView, false)

        val text = badge.findViewById<TextView>(R.id.tv_badge)
        text.text = value.toString()
        itemView.addView(badge)
    }

    fun removeBadge(@IdRes itemId: Int) {
        val itemView = mBottomNavigationView.findViewById<BottomNavigationItemView>(itemId)
        if (itemView.childCount == 3) {
            itemView.removeViewAt(2)
        }
    }

}

