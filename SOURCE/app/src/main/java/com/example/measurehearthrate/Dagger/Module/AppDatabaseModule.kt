package com.example.measurehearthrate.Dagger.Module

import androidx.room.Room
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Database.UserDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDatabaseModule  {

    @Singleton
    @Provides
    fun providesUserDao(db: UserDatabase) = db.userDAO()

    @Singleton
    @Provides
    fun provideUserDatabase(app: MyApplication): UserDatabase {
        return Room.databaseBuilder(app,UserDatabase::class.java,"USER DATABASE")
                .addMigrations(UserDatabase.migration1To2())
                .build()
    }
}