package com.example.hearthrate.Interface.RoomDB

import androidx.room.*
import com.example.hearthrate.Model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user : User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)

    @Query("SELECT * FROM USER WHERE id =:id " )
    fun getUser(id : String) : User

    @Query("SELECT * FROM USER WHERE email =:email")
    fun getUserByEmail(email: String) : User?
}