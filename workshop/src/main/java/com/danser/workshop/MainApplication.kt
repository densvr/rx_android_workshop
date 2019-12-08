package com.danser.workshop

import android.app.Application

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MAIN_COMPONENT = MainComponent(
            applicationContext.resources
        )
    }

    companion object {
        lateinit var MAIN_COMPONENT: MainComponent
    }
}
