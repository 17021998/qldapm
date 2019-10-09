package com.example.measurehearthrate.Database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.measurehearthrate.Interface.RoomDB.UserDao
import com.example.measurehearthrate.Model.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {


    abstract fun userDAO(): UserDao

    companion object {
        const val TAG = "UserDatabase"

        fun migration1To2() : Migration =object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.beginTransaction()
                database.run {
                    try {
                        execSQL("CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL, avatar TEXT, firstname TEXT, lastname TEXT, username TEXT, gender TEXT, phone TEXT, birthday TEXT)")

                    } catch (ex: Exception) {
                        Log.e(TAG, "$ex, migration is failed")
                    }
                }
                database.setTransactionSuccessful()
                database.endTransaction()
            }
        }
    }


}