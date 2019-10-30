package com.example.hearthrate.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "USER")
data class User(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") var mId: Long = 0,
        @ColumnInfo(name = "email") var mEmail: String ?= null,
        @ColumnInfo(name = "password") var mPassword: String ?= null,
        @ColumnInfo(name = "avatar") var mAvatar: String? = null,
        @ColumnInfo(name = "firstname") var mFirstName: String ?= null,
        @ColumnInfo(name = "lastname") var mLastName: String ?= null,
        @ColumnInfo(name = "gender") var mGender: String? = null,
        @ColumnInfo(name = "birthday") var mBirthday: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            mId = parcel.readLong(),
            mEmail = parcel.readString(),
            mPassword = parcel.readString(),
            mAvatar = parcel.readString(),
            mFirstName = parcel.readString(),
            mLastName = parcel.readString(),
            mGender = parcel.readString(),
            mBirthday = parcel.readString())

    constructor(mEmail: String, mPassword: String, mFirstName: String, mLastName: String,  mGender: String, mBirthday: String) : this() {
        this.mFirstName = mFirstName
        this.mLastName = mLastName
        this.mEmail = mEmail
        this.mPassword = mPassword
        this.mGender = mGender
        this.mBirthday = mBirthday
    }

    constructor(mEmail: String, mPassword: String) : this() {
        this.mEmail = mEmail
        this.mPassword = mPassword
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(mId)
        parcel.writeString(mEmail)
        parcel.writeString(mPassword)
        parcel.writeString(mAvatar)
        parcel.writeString(mFirstName)
        parcel.writeString(mLastName)
        parcel.writeString(mGender)
        parcel.writeString(mBirthday)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }




}