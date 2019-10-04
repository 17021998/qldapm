package com.example.measurehearthrate.Base

import android.app.Application
import com.example.measurehearthrate.Dagger.Component.AppComponent
import com.example.measurehearthrate.Dagger.Component.DaggerAppComponent
import com.example.measurehearthrate.Dagger.Module.AppModule
import dagger.android.support.DaggerAppCompatActivity

open class MyApplication : Application(){

    lateinit var appComponent: AppComponent

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        appComponent.inject(this)
    }
}