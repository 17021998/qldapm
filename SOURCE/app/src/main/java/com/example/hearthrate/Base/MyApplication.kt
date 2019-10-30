package com.example.hearthrate.Base

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.example.hearthrate.Dagger.Component.AppComponent
import com.example.hearthrate.Dagger.Component.DaggerAppComponent
import com.example.hearthrate.Dagger.Module.AppModule
import com.example.hearthrate.Database.UserDatabase

open class MyApplication : Application(){

    lateinit var appComponent: AppComponent

    companion object {
        lateinit var instance: MyApplication
        lateinit var userDatabase: UserDatabase
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        userDatabase = Room.databaseBuilder(this,UserDatabase::class.java,"USER DATABASE")
        .addMigrations(UserDatabase.migration1To2())
                .build()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        appComponent.inject(this)
    }

    fun hasNetworkConnection(): Boolean {
        val conectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}