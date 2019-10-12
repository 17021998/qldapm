package com.example.measurehearthrate.ViewModel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.measurehearthrate.Helper.ValidationHelper
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Base.BaseViewModel
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Database.UserDatabase
import com.example.measurehearthrate.Helper.AuthenticationEmailExistingHelper
import com.example.measurehearthrate.Helper.LanguagesHelper
import com.example.measurehearthrate.Model.User
import com.example.measurehearthrate.Utils.CoroutineUsecase
import com.example.measurehearthrate.Utils.MD5Algorithm
import kotlinx.coroutines.launch
import javax.inject.Inject


class SignUpViewModel @Inject constructor(private val userDatabase: UserDatabase) : BaseViewModel() {


    private var mEmail: String? = null
    private var mPassword: String? = null

    private var mEmailState: MutableLiveData<UiEmailWrapper> = MutableLiveData()
    private var mPasswordState: MutableLiveData<UiPassWordWrapper> = MutableLiveData()
    private var mSignUpButtonState: MutableLiveData<UiSignUpButtonWrapper> = MutableLiveData()

    @Inject
    lateinit var mAuthenticationEmailExisting: AuthenticationEmailExistingHelper


    val emailState: LiveData<UiEmailWrapper>
        get() = mEmailState

    val passwordState: LiveData<UiPassWordWrapper>
        get() = mPasswordState

    val btnSignUpState: LiveData<UiSignUpButtonWrapper>
        get() = mSignUpButtonState

    companion object {
        val TAG = SignUpViewModel::class.java.simpleName
    }

    override fun start() {
        isEnableSignUpButton()
    }

    fun register(email: String, pass: String) {

        mAuthenticationEmailExisting.executeUsecase(AuthenticationEmailExistingHelper.RequestValues(email),
                object : CoroutineUsecase.UseCaseCallBack<AuthenticationEmailExistingHelper.ResponseValue, AuthenticationEmailExistingHelper.ResponseError> {
                    override fun onSuccess(responseValue: AuthenticationEmailExistingHelper.ResponseValue) {
                        when (responseValue.isExisting) {
                            true -> { // email tồn tại
                                emitEmailState(false, true, LanguagesHelper.getString(MyApplication.instance,R.string.SignUp_EmailInUse_Text__ThisEmailIsAlreadyInUse))
                            }
                            false -> {
                                mEmail = email
                                mPassword = MD5Algorithm.md5Encrypt(pass)
                                Log.d(TAG,"SignupViewModel - Password encrypted: $mPassword")

                                emitBtnSignUpState(true, true)
                            }

                        }
                    }

                    override fun onError(errorValue: AuthenticationEmailExistingHelper.ResponseError) {
                    }

                })
    }


    fun onEmailTextChanged(email: String) {
        mEmail = email
        isEmailValid()
        isEnableSignUpButton()
    }

    private fun isEmailValid(): Boolean {
        return if (mEmail.isNullOrEmpty()) {
            emitEmailState(false, false, "")
            false
        } else if (!ValidationHelper.isEmailValid(mEmail!!)) {
            emitEmailState(false, true, LanguagesHelper.getString(MyApplication.instance, R.string.SignUp_InputError_Text__InvalidEmailAddress))
            false
        } else {
            emitEmailState(true, false, "")
            true
        }

    }

    fun onPasswordChanged(pass: String? = mPassword) {
        mPassword = pass
        isPassValid()
        isEnableSignUpButton()
    }

    private fun isPassValid(): Boolean {
        return if (mPassword.isNullOrEmpty()) {
            emitPasswordState(false, false)
            false
        } else {
            val isMinCharMet = ValidationHelper.isMinPasswordVilid(mPassword!!)
            val isFormatMet = ValidationHelper.isPasswordFormatdValid(mPassword!!)
            emitPasswordState(isMinCharMet, isFormatMet)
            isMinCharMet && isFormatMet
        }
    }

    fun isEnableSignUpButton() {
        val isEmailPasswordEmpty = TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword)
        val isEmailMet = isEmailValid()
        val isPasswordMet = isPassValid()

        if (!isEmailPasswordEmpty && isPasswordMet && isEmailMet) {
            emitBtnSignUpState(false, true)
        } else {
            emitBtnSignUpState(false, false)
        }
    }

    private fun emitEmailState(
            isValid: Boolean? = null,
            enableError: Boolean = false,
            errorMessage: String? = null

    ) {
        val uiModelWrapper = UiEmailWrapper(isValid, enableError, errorMessage)
        mEmailState.postValue(uiModelWrapper)
    }

    private fun emitPasswordState(
            isMinPassMet: Boolean = false,
            isPassFormatMet: Boolean = false) {
        val uiModelWrapper = UiPassWordWrapper(isMinPassMet, isPassFormatMet)
        mPasswordState.postValue(uiModelWrapper)
    }

    private fun emitBtnSignUpState(
            isSignUpSuccess: Boolean = false,
            isEnable: Boolean = false
    ) {
        val uiBtnSignUpModel = UiSignUpButtonWrapper(isSignUpSuccess, isEnable)
        mSignUpButtonState.postValue(uiBtnSignUpModel)
    }

    data class UiEmailWrapper(

            var isValid: Boolean?,
            var enableError: Boolean,
            var errorMessage: String?

    )

    data class UiPassWordWrapper(
            var isMinPasswordMet: Boolean,
            var isPasswordFormatMet: Boolean
    )


    data class UiSignUpButtonWrapper(
            var isSignUpSuccess: Boolean,
            var isEnable: Boolean
    )


}