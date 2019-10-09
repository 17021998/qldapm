package com.example.measurehearthrate.ViewModel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.measurehearthrate.Base.BaseViewModel
import com.example.measurehearthrate.Helper.ValidationHelper
import javax.inject.Inject

class SigninViewModel @Inject constructor(): BaseViewModel() {


    private var mEmail: String?=null
    private var mPassword: String?= null

    private var mBtnLoginState : MutableLiveData<UiBtnLoginWrapper> = MutableLiveData()

    val btnLoginState : LiveData<UiBtnLoginWrapper>
        get() = mBtnLoginState

    override fun start() {
    }

    fun login() {
        emitBtnLoginState(true,true)
    }



    fun onEmailTextChanged(email: String) {
        mEmail = email
        isEmailValid()
    }

    fun onPasswordTextChanged(pass: String) {
        mPassword = pass
    }

    private fun isEmailValid() : Boolean {
        return if(mEmail.isNullOrEmpty()) {
            emitBtnLoginState(false,false,false)
            false
        } else if(!ValidationHelper.isEmailValid(mEmail!!)) {
            emitBtnLoginState(false, false,false)
            false
        }
        else {
            emitBtnLoginState(false,true,false)
            true
        }
    }

    fun isEnableSignInButton() {
        val isEmailPassEmpty = !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)
        val isEmailValid = ValidationHelper.isEmailValid(mEmail!!)

        if(isEmailPassEmpty && isEmailValid) {
            emitBtnLoginState(false, true,true)
        }
    }


    private fun emitBtnLoginState(
            click : Boolean = false,
            enableChecked: Boolean = false,
            enable : Boolean = false
    ) {
        val uiBtnLoginModel = UiBtnLoginWrapper(click,enableChecked,enable)
        mBtnLoginState.postValue(uiBtnLoginModel)
    }

    data class UiBtnLoginWrapper (
            var isClick: Boolean,
            var enableChecked: Boolean,
            var isEnable: Boolean
    )
}