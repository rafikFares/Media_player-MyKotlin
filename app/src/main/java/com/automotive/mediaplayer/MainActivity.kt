package com.automotive.mediaplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.automotive.mediaplayer.ui.BottomBadgeHelper
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var mediaViewModel: MediaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_titles, R.id.navigation_albums
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        BottomBadgeHelper.initialize(navView)

        mediaViewModel = ViewModelProviders.of(this).get(MediaViewModel::class.java)

        mediaViewModel.audioListSize.observe(this, Observer {
            BottomBadgeHelper.showBadge(R.id.navigation_titles, it, this)
        })


    }

    override fun onResume() {
        super.onResume()




        //mMediaPresenter.playAudio("http://player.zebradio.fr:8000/player.mp3")
    }

    fun List<Int>.allNonZero() =  all { 0 !in this }
    fun List<Int>.allNonZero1() =  none { TODO() }
    fun List<Int>.allNonZero2() =  any { TODO() }

    fun List<Int>.containsZero() =  any { TODO() }
    fun List<Int>.containsZero1() =  all { TODO() }
    fun List<Int>.containsZero2() =  none { TODO() }
}
