package com.example.measurehearthrate.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "USER")
data class User(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") var mId: Long = 0,
        @ColumnInfo(name = "email") var mEmail: String = "",
        @ColumnInfo(name = "password") var mPassword: String = "",
        @ColumnInfo(name = "avatar") var mAvatar: String? = null,
        @ColumnInfo(name = "firstname") var mFirstName: String = "",
        @ColumnInfo(name = "lastname") var mLastName: String = "",
        @ColumnInfo(name = "username") var mUsername: String = "",
        @ColumnInfo(name = "gender") var mGender: Boolean? = null,
        @ColumnInfo(name = "phone") var mPhone: String? = null,
        @ColumnInfo(name = "birthday") var mBirthday: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString()) {
    }




    constructor(mFirstName: String, mLastName: String, mUsername: String, mEmail: String, mPassword: String, mGender: Boolean?, mPhone: String?, mBirthday: String?) : this() {
        this.mFirstName = mFirstName
        this.mLastName = mLastName
        this.mUsername = mUsername
        this.mEmail = mEmail
        this.mPassword = mPassword
        this.mGender = mGender
        this.mPhone = mPhone
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
        parcel.writeString(mUsername)
        parcel.writeValue(mGender)
        parcel.writeString(mPhone)
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