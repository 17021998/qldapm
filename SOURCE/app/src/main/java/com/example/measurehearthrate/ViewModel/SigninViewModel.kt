package com.example.measurehearthrate.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.measurehearthrate.Base.BaseViewModel
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Helper.DialogHelper
import com.example.measurehearthrate.Helper.LanguagesHelper
import com.example.measurehearthrate.Helper.ValidationHelper
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Usecase.SigninUsecase
import com.example.measurehearthrate.Utils.CoroutineUsecase
import com.example.measurehearthrate.Utils.ResponseCode
import com.example.measurehearthrate.Utils.TextUtils
import javax.inject.Inject

class SigninViewModel @Inject constructor(private var mSigninUsecase: SigninUsecase) : BaseViewModel() {

    companion object {
        const val TAG = "SigninViewModel"
    }

    private var mEmail: String? = null
    private var mPassword: String? = null

    private var mLoginState: MutableLiveData<UiLoginWrapper> = MutableLiveData()
    //private var mDialogState: MutableLiveData<DialogWrapper> = MutableLiveData()

    val loginState: LiveData<UiLoginWrapper>
        get() = mLoginState

//    val dialogState: LiveData<DialogWrapper>
//        get() = mDialogState

    override fun start() {
    }

    fun login(email: String, pass: String) {
        mEmail = email
        mPassword = pass

        DialogHelper.emitDialogState(true)
        mSigninUsecase.executeUsecase(SigninUsecase.RequestValue(email, pass),
                object : CoroutineUsecase.UseCaseCallBack<SigninUsecase.ResponseValue, SigninUsecase.ErrorValue> {
                    override fun onSuccess(responseValue: SigninUsecase.ResponseValue) {
                        DialogHelper.emitDialogState(false)
                        emitLoginState(true, false, false)
                    }

                    override fun onError(errorValue: SigninUsecase.ErrorValue) {
                        DialogHelper.emitDialogState(false)
                        when (errorValue.errorCode) {
                            ResponseCode.WRONG_PASSWORD -> {
                                emitLoginState(false, true, true, false, true)
                            }
                            ResponseCode.WRONG_EMAIL -> {
                                emitLoginState(false, true, true, true, null ,LanguagesHelper.getString(MyApplication.instance, R.string.Login_Text__EmailIsNotRegister))

                            }
                        }
                    }

                })
    }

    fun onEmailTextChanged(email: String) {
        mEmail = email
        isEmailValid()
        isEnableSignInButton()
    }

    fun onPasswordTextChanged(pass: String) {
        mPassword = pass
        isEnableSignInButton()
    }

    private fun isEmailValid(): Boolean {
        return if (mEmail.isNullOrEmpty()) {
            emitLoginState(null, false, false)
            false
        } else if (!ValidationHelper.isEmailValid(mEmail!!)) {
            emitLoginState(null, false, false, true,null ,LanguagesHelper.getString(MyApplication.instance, R.string.SignUp_InputError_Text__InvalidEmailAddress))
            false
        } else {
            emitLoginState(null, true, false,false,null,null)
            true
        }
    }

    fun isEnableSignInButton() {
        val isEmailPassEmpty = !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)
        val isEmailValid = ValidationHelper.isEmailValid(mEmail)

        if (isEmailPassEmpty && isEmailValid) {
            emitLoginState(null, true, true,false,null,null)
        }
    }


    private fun emitLoginState(
            isLoginSuccess: Boolean? = null,
            enableEmailCheck: Boolean = false,
            enableBtnLogin: Boolean = false,
            wrongEmail: Boolean = false ,
            wrongPassword: Boolean? = null,
            emailError: String?= null
    ) {
        val uiLoginModel = UiLoginWrapper(isLoginSuccess, enableEmailCheck, enableBtnLogin, wrongEmail, wrongPassword,emailError)
        mLoginState.postValue(uiLoginModel)
    }

    data class UiLoginWrapper(
            var isLoginSuccess: Boolean?,
            var enableChecked: Boolean,
            var isEnable: Boolean,
            var wrongEmail: Boolean,
            var wrongPassword: Boolean?,
            var emailError: String?
    )


}