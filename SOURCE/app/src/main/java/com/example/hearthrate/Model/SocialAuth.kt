package com.example.hearthrate.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class SocialAuth (@PrimaryKey(autoGenerate = true)
                  @ColumnInfo(name = "id") var id: String,
                  @field:SerializedName("email") var email: String,
                  @field:SerializedName("token") var token: String,
                  @field:SerializedName("clientId") var clientId: String,
                  @field:SerializedName("firstName") var firstName: String,
                  @field:SerializedName("lastName") var lastName: String,
                  @field:SerializedName("birthday") var birthday: String,
                  @field:SerializedName("gender") var gender: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    constructor(): this("","","","","","","","")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(token)
        parcel.writeString(clientId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(birthday)
        parcel.writeString(gender)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SocialAuth> {
        override fun createFromParcel(parcel: Parcel): SocialAuth {
            return SocialAuth(parcel)
        }

        override fun newArray(size: Int): Array<SocialAuth?> {
            return arrayOfNulls(size)
        }
    }

}