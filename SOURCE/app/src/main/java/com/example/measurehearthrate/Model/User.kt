package com.example.measurehearthrate.Model

import android.net.Uri
import com.example.measurehearthrate.Helper.DateHelper
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class User {

    //var mBirthday: Date = SimpleDateFormat(DateHelper.FORMAT_SHORT_DATE).parse(DateHelper.getCurrentDay())

    var mAvatar: Uri?= null
    var mFirstName: String = ""
    var mLastName: String = ""
    var mUsername: String = ""
    var mEmail: String =""
    var mPassword: String = ""
    var mGender: Boolean ?=null
    var mPhone: String ?= null
    var mBirthday: Date? =null

    constructor(mFirstName: String, mLastName: String, mUsername: String, mEmail: String, mPassword: String, mGender: Boolean?, mPhone: String?, mBirthday: Date?) {
        this.mFirstName = mFirstName
        this.mLastName = mLastName
        this.mUsername = mUsername
        this.mEmail = mEmail
        this.mPassword = mPassword
        this.mGender = mGender
        this.mPhone = mPhone
        this.mBirthday = mBirthday
    }


}