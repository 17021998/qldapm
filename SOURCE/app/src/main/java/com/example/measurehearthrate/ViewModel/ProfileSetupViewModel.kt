package com.example.measurehearthrate.ViewModel

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.measurehearthrate.Base.BaseViewModel
import com.example.measurehearthrate.Database.UserDatabase
import com.example.measurehearthrate.Helper.DateHelper
import com.example.measurehearthrate.Helper.DialogHelper
import com.example.measurehearthrate.Model.User
import com.example.measurehearthrate.Utils.Gender
import io.reactivex.internal.operators.single.SingleDoOnSuccess
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ProfileSetupViewModel @Inject constructor(private val userDatabase: UserDatabase) : BaseViewModel() {

    private var mEmail: String ?= null
    private var mPassword: String ?= null
    private var mFirstName: String? = null
    private var mLastName: String? = null
    private var mGender: String? = null
    private var mBirthDay: String? = null
    private var mDataUsageChecked: Boolean = false

    private var mUiState: MutableLiveData<UIWrapper> = MutableLiveData()

    val uiState: LiveData<UIWrapper>
        get() = mUiState

    override fun start() {

    }

    fun onFirstNameChanged(firstname: String) {
        mFirstName = firstname
        if (isFirstNameValid)
            isEnableBtnCreateAccount()
    }

    private val isFirstNameValid: Boolean
        get() {
            return !TextUtils.isEmpty(mFirstName)
        }

    fun onLastNameChanged(lastname: String) {
        mLastName = lastname
        if(isLastNameValid)
            isEnableBtnCreateAccount()

    }

    private val isLastNameValid: Boolean
        get() {
            return !TextUtils.isEmpty(mLastName)
        }


    fun onGenderChanged(gender: Gender) {
        mGender = gender.toString()
        //emitUiState(!mFirstName.isNullOrEmpty(), !mLastName.isNullOrEmpty(), false, gender)
        isEnableBtnCreateAccount()
    }

    fun onCbDataUsageClick(checked: Boolean) {
        mDataUsageChecked = checked
        isEnableBtnCreateAccount()
    }

    fun onBirthDaySelected(calendar: Calendar?) {
        val date = calendar!!.time
        mBirthDay = DateHelper.formatShortDate(date)
        isEnableBtnCreateAccount()
    }

    fun isEnableBtnCreateAccount() {
        if (mDataUsageChecked && !TextUtils.isEmpty(mBirthDay) && !TextUtils.isEmpty(mGender) && isFirstNameValid && isLastNameValid) {
            emitUiState(
                    !mFirstName.isNullOrEmpty(),
                    !mLastName.isNullOrEmpty(),
                    !mBirthDay.isNullOrEmpty(),
                    mGender?.let { Gender.getObjectGender(it) },
                    true)
        } else {
            emitUiState(
                    !mFirstName.isNullOrEmpty(),
                    !mLastName.isNullOrEmpty(),
                    !mBirthDay.isNullOrEmpty(),
                    mGender?.let { Gender.getObjectGender(it) },
                    false)
        }
    }

    fun getBirthDay(): String? {
        return mBirthDay
    }

    fun createAccountWithEmail() {
        DialogHelper.emitDialogState(true)
        val user = User(mEmail!!,mPassword!!,mFirstName!!,mLastName!!,mGender!!,mBirthDay!!)
        bgScope.launch {
            userDatabase.userDAO().insertUser(user)
        }

        emitUiState(
                !mFirstName.isNullOrEmpty(),
                !mLastName.isNullOrEmpty(),
                !mBirthDay.isNullOrEmpty(),
                mGender?.let { Gender.getObjectGender(it) },
                true,
                true)

        DialogHelper.emitDialogState(false)
    }

    fun receiveEmailPassword(email: String?, password: String?) {
        mEmail = email
        mPassword = password
    }

    fun emitUiState(
            isFirstnameEmpty: Boolean = false,
            isLastnamEmpty: Boolean = false,
            birthdayClick: Boolean = false,
            genderChose: Gender? = null,
            isEnableBtnCreate: Boolean = false,
            isCreateSuccess: Boolean = false
    ) {
        val uiModel = UIWrapper(isFirstnameEmpty, isLastnamEmpty, birthdayClick, genderChose, isEnableBtnCreate,isCreateSuccess)
        mUiState.postValue(uiModel)
    }


    data class UIWrapper(
            var isFirstnameEmpty: Boolean,
            var isLastnamEmpty: Boolean,
            var birthdayClick: Boolean,
            var genderChose: Gender?,
            var isEnableBtnCreate: Boolean,
            var isCreateSuccess: Boolean

    )

}