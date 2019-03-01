package com.example.moviespaging.app

import android.app.Application
import org.koin.android.ext.android.startKoin
import com.example.moviespaging.R
import com.example.moviespaging.di.KoinModules

class MyApplication : Application() {

    override fun onCreate(){
        super.onCreate()
        startKoin(this, listOf(KoinModules.koinModule))
    }
}