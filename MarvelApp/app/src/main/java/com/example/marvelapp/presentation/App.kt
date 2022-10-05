package com.example.marvelapp.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
//    companion object {
//        lateinit var instance: App
//    }
//
//    private val cicerone: Cicerone<Router> by lazy {
//        Cicerone.create()
//    }
//    val navigatorHolder get() = cicerone.getNavigatorHolder()
//    val router get() = cicerone.router
//
//    override fun onCreate() {
//        super.onCreate()
//        instance = this
//    }
}