package com.example.measurehearthrate.ViewModel

import com.example.measurehearthrate.Base.BaseViewModel
import com.example.measurehearthrate.Database.UserDatabase
import com.example.measurehearthrate.Model.User
import javax.inject.Inject

class ProfileSetupViewModel @Inject constructor(): BaseViewModel() {

    private lateinit var user : User

    private var mEmail: String = ""
    private var mFirstName: String? = null
    private var mLastName: String? = null
    private var mGender: String? = null
    private var mBirthDay: String? = null

    override fun start() {

    }




}