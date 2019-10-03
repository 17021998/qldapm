package com.example.measurehearthrate.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.measurehearthrate.Helper.ValidationHelper
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Base.BaseActivity
import com.example.measurehearthrate.Base.BaseViewModel
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpViewModel @Inject internal  constructor(private val mContext: BaseActivity): BaseViewModel(){

    private var mEmail: String? = null
    private var mPassword: String? = null

    private var mUiState: MutableLiveData<UIModelWrapper> = MutableLiveData()

    val uiState: LiveData<UIModelWrapper>
            get() = mUiState

    companion object {
        val TAG = SignUpViewModel::class.java.simpleName
        const val MINIMUM_PASSWORD_LENGTH = 7
        val passWordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z]).+)")!!
    }

    fun register() {
        emitUiState(true)
    }

    private fun emitUiState(
            register: Boolean = false,
            isValid: Boolean ?= null,
            enableError: Boolean?= null,
            errorMessage: String?= null
    ) {
        val uiModelWrapper = UIModelWrapper(register,isValid,enableError,errorMessage)
        mUiState.postValue(uiModelWrapper)
    }

    fun onEmailTextChanged(email: String) {
        mEmail = email
        isEmailValid()

    }

    private fun isEmailValid() : Boolean {
        return if( mEmail.isNullOrEmpty()) {
            emitUiState(false,false,false,"")
            false
        } else if(!ValidationHelper.isEmailValid(mEmail!!)) {
            emitUiState(false,false,false,mContext.resources.getString(R.string.SignUp_InputError_Text__InvalidEmailAddress))
            false
        } else {
            emitUiState(false,true,false,"")
            true
        }

    }

    data class UIModelWrapper(
            var register: Boolean,
            var isValid: Boolean?,
            var enableError: Boolean?,
            var errorMessage: String?
    )




}