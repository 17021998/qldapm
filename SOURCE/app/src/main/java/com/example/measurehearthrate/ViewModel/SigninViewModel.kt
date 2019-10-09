package com.example.measurehearthrate.ViewModel

import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.measurehearthrate.Base.BaseViewModel
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Helper.ValidationHelper
import com.example.measurehearthrate.Usecase.SigninUsecase
import com.example.measurehearthrate.Utils.CoroutineUsecase
import com.example.measurehearthrate.Utils.ResponseCode
import javax.inject.Inject

class SigninViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var mSigninUsecase: SigninUsecase

    private var mEmail: String? = null
    private var mPassword: String? = null

    private var mBtnLoginState: MutableLiveData<UiBtnLoginWrapper> = MutableLiveData()

    val btnLoginState: LiveData<UiBtnLoginWrapper>
        get() = mBtnLoginState

    override fun start() {
    }

    fun login(email: String, pass: String) {
        mEmail = email
        mPassword = pass

        mSigninUsecase.executeUsecase(SigninUsecase.RequestValue(email, pass),
                object : CoroutineUsecase.UseCaseCallBack<SigninUsecase.ResponseValue, SigninUsecase.ErrorValue> {
                    override fun onSuccess(responseValue: SigninUsecase.ResponseValue) {
                            emitLoginState(true, false, false)
                    }

                    override fun onError(errorValue: SigninUsecase.ErrorValue) {
                        when (errorValue.errorCode) {
                            ResponseCode.ERROR_PASSWORD -> {
                                emitLoginState(false, true, true,false,true)
                            }
                            ResponseCode.WRONG_EMAIL -> {
                                emitLoginState(false, true, true,true)

                            }
                        }
                    }

                })
        //emitLoginState(true,true)
    }

    fun onEmailTextChanged(email: String) {
        mEmail = email
        isEmailValid()
    }

    fun onPasswordTextChanged(pass: String) {
        mPassword = pass
    }

    private fun isEmailValid(): Boolean {
        return if (mEmail.isNullOrEmpty()) {
            emitLoginState(null, false, false)
            false
        } else if (!ValidationHelper.isEmailValid(mEmail!!)) {
            emitLoginState(null, false, false)
            false
        } else {
            emitLoginState(null, true, false)
            true
        }
    }

    fun isEnableSignInButton() {
        val isEmailPassEmpty = !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)
        val isEmailValid = ValidationHelper.isEmailValid(mEmail!!)

        if (isEmailPassEmpty && isEmailValid) {
            emitLoginState(null,true, true)
        }
    }


    private fun emitLoginState(
            isLoginSuccess: Boolean ?= null,
            enableEmailCheck: Boolean = false,
            enableBtnLogin: Boolean = false,
            wrongEmail: Boolean = false,
            wrongPassword: Boolean = false
    ) {
        val uiBtnLoginModel = UiBtnLoginWrapper(isLoginSuccess, enableEmailCheck, enableBtnLogin,wrongEmail,wrongPassword)
        mBtnLoginState.postValue(uiBtnLoginModel)
    }

    data class UiBtnLoginWrapper(
            var isLoginSuccess: Boolean?,
            var enableChecked: Boolean,
            var isEnable: Boolean,
            var wrongEmail: Boolean,
            var wrongPassword: Boolean
    )
}