package com.example.measurehearthrate.Base

import android.app.Application
import androidx.room.Room
import com.example.measurehearthrate.Dagger.Component.AppComponent
import com.example.measurehearthrate.Dagger.Component.DaggerAppComponent
import com.example.measurehearthrate.Dagger.Module.AppModule
import com.example.measurehearthrate.Database.UserDatabase

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
}