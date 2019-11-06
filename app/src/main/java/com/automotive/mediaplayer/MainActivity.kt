package com.automotive.mediaplayer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.automotive.mediaplayer.ui.BottomBadgeHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.system.sound.notification.NotificationHelper
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val mediaViewModel: MediaViewModel by viewModels()
    @Inject
    lateinit var notificationHelper: NotificationHelper

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

        //mediaViewModel = ViewModelProviders.of(this).get(MediaViewModel::class.java)

        mediaViewModel.audioListSize.observe(this, Observer {
            BottomBadgeHelper.showBadge(R.id.navigation_titles, it, this)
        })

        lifecycleScope.launchWhenResumed {
            notificationCounter("Resumed ")
        }

    }

    private suspend fun notificationCounter(name: String) = try {
        for (i in 1..Int.MAX_VALUE) {
            notificationHelper.showNotification(title = name + i, notificationId = 9999)
        }
    } catch (e: Exception) {
        notificationHelper.hideNotification(9999)
    }

    override fun onResume() {
        super.onResume()

        //mMediaPresenter.playAudio("http://")
    }


}
