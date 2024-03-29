package com.system.sound.injection

import android.app.Application
import android.util.Log.d
import com.system.sound.injection.module.*

class MediaServiceApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        d("MediaServiceApplication", ">>>>>>>>>>>>>>>>>>>>> onCreate ")
        appComponent = initDagger(this)
    }

    private fun initDagger(app: Application) = DaggerAppComponent.builder()
        .appModule(AppModule(app))
        .mediaPresenterModule(MediaPresenterModule())
        .notificationHelperModule(NotificationHelperModule())
        .serviceRepositoryModule(ServiceRepositoryModule())
        .filesRepositoryModule(FilesRepositoryModule())
        .build()


}