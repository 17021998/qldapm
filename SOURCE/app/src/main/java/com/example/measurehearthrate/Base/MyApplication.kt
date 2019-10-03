package com.example.measurehearthrate.Base

import android.app.Application
import com.example.measurehearthrate.Dagger.Component.AppComponent
import dagger.android.support.DaggerAppCompatActivity

open class MyApplication : Application(){

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        
        //appComponent = DaggerAppComponent

    }
}