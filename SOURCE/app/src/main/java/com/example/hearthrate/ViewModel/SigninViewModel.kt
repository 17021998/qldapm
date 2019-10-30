package com.example.hearthrate.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Base.BaseViewModel
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.Helper.DialogHelper
import com.example.hearthrate.Helper.LanguagesHelper
import com.example.hearthrate.Helper.ValidationHelper
import com.example.hearthrate.R
import com.example.hearthrate.Usecase.SigninGoogleUsecase
import com.example.hearthrate.Usecase.SigninUsecase
import com.example.hearthrate.Utils.CoroutineUsecase
import com.example.hearthrate.Utils.ErrorCode
import com.example.hearthrate.Utils.TextUtils
import java.lang.ref.WeakReference
import javax.inject.Inject

class SigninViewModel @Inject constructor(private var mContext: BaseActivity,
                                          private var mSigninUsecase: SigninUsecase,
                                          private var mSigninGoogleUsecase: SigninGoogleUsecase) : BaseViewModel() {

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
        // if(MyApplication.instance.hasNetworkConnection()) {
        mEmail = email
        mPassword = pass

        DialogHelper.emitDialogState(true)
        mSigninUsecase.executeUsecase(SigninUsecase.RequestValue(email, pass),
                object : CoroutineUsecase.UseCaseCallBack<SigninUsecase.ResponseValue, SigninUsecase.ErrorValue> {
                    override fun onSuccess(responseValue: SigninUsecase.ResponseValue) {
                        DialogHelper.emitDialogState(false)
                        emitLoginState(true, false, false, false, null, null)
                    }

                    override fun onError(errorValue: SigninUsecase.ErrorValue) {
                        DialogHelper.emitDialogState(false)
                        when (errorValue.errorCode) {
                            ErrorCode.WRONG_PASSWORD -> {
                                emitLoginState(false, true, true, false, true)
                            }
                            ErrorCode.WRONG_EMAIL -> {
                                emitLoginState(false, true, true, true, null, LanguagesHelper.getString(MyApplication.instance, R.string.Login_Text__EmailIsNotRegister))

                            }
                        }
                    }

                })
//        } else {
//            emitLoginState(false, true, true, false, false,null, ErrorCode.NO_INTERNET_CONNECTION)
//        }

    }

    fun loginWithGoogle() {
        //if(MyApplication.instance.hasNetworkConnection()) {
        DialogHelper.emitDialogState(true)
        mSigninGoogleUsecase.executeUsecase(SigninGoogleUsecase.RequestValue(WeakReference(mContext)),
                object : CoroutineUsecase.UseCaseCallBack<SigninGoogleUsecase.ResponseValue,SigninGoogleUsecase.ResponseError> {
                    override fun onSuccess(responseValue: SigninGoogleUsecase.ResponseValue) {

                    }

                    override fun onError(errorValue: SigninGoogleUsecase.ResponseError) {

                    }

                })


        //} else {
        //            emitLoginState(false, true, true, false, false,null, ErrorCode.NO_INTERNET_CONNECTION)

        //}
    }

    fun loginWithFacebook() {
        //if(MyApplication.instance.hasNetworkConnection()) {


        //} else {
        //            emitLoginState(false, true, true, false, false,null, ErrorCode.NO_INTERNET_CONNECTION)

        //}
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
            emitLoginState(null, false, false, true, null, LanguagesHelper.getString(MyApplication.instance, R.string.SignUp_InputError_Text__InvalidEmailAddress))
            false
        } else {
            emitLoginState(null, true, false, false, null, null)
            true
        }
    }

    fun isEnableSignInButton() {
        val isEmailPassEmpty = !TextUtils.isEmpty(mEmail) && !TextUtils.isEmpty(mPassword)
        val isEmailValid = ValidationHelper.isEmailValid(mEmail)

        if (isEmailPassEmpty && isEmailValid) {
            emitLoginState(null, true, true, false, null, null)
        }
    }


    private fun emitLoginState(
            isLoginSuccess: Boolean? = null,
            enableEmailCheck: Boolean = false,
            enableBtnLogin: Boolean = false,
            wrongEmail: Boolean = false,
            wrongPassword: Boolean? = null,
            emailError: String? = null,
            serverError: Int? = null
    ) {
        val uiLoginModel = UiLoginWrapper(isLoginSuccess, enableEmailCheck, enableBtnLogin, wrongEmail, wrongPassword, emailError, serverError)
        mLoginState.postValue(uiLoginModel)
    }


    data class UiLoginWrapper(
            var isLoginSuccess: Boolean?,
            var enableChecked: Boolean,
            var isEnable: Boolean,
            var wrongEmail: Boolean,
            var wrongPassword: Boolean?,
            var emailError: String?,
            var serverError: Int?
    )


}