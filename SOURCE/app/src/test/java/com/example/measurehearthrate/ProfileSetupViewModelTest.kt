package com.example.measurehearthrate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.measurehearthrate.Database.UserDatabase
import com.example.measurehearthrate.Helper.DialogHelper
import com.example.measurehearthrate.Interface.RoomDB.UserDao
import com.example.measurehearthrate.Utils.Gender
import com.example.measurehearthrate.Utils.LiveDataTestUtils
import com.example.measurehearthrate.ViewModel.ProfileSetupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.util.*

@RunWith(PowerMockRunner::class)
class ProfileSetupViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mUserDatabase: UserDatabase

    @Mock
    private lateinit var userDao: UserDao

    private lateinit var mViewModel: ProfileSetupViewModel

    companion object {
        val showDialog = DialogHelper.DialogWrapper(
                isshowingDialog = true
        )

        val hideDialog = DialogHelper.DialogWrapper(
                isshowingDialog = false
        )
    }

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @Before
    fun start() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        mViewModel = ProfileSetupViewModel(mUserDatabase)
    }

    @After
    fun stop() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun onFirstNameChanged_FistnameNull() {
        mViewModel.onFirstNameChanged("")
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == null)
    }

    @Test
    fun onFirstNameChanged_FistnameNotNull() {
        mViewModel.onFirstNameChanged("phong")
        val expectedUiState = ProfileSetupViewModel.UIWrapper(
                isFirstnameEmpty = false,
                isLastnamEmpty = true,
                birthdayNull = true,
                genderChose = null,
                isEnableBtnCreate = false,
                isCreateSuccess = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == expectedUiState)
    }

    @Test
    fun onLastNameChanged_LastnameNull() {
        mViewModel.onLastNameChanged("")
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == null)
    }

    @Test
    fun onLastNameChanged_FistnameNotNull() {
        mViewModel.onLastNameChanged("phong")
        val expectedUiState = ProfileSetupViewModel.UIWrapper(
                isFirstnameEmpty = true,
                isLastnamEmpty = false,
                birthdayNull = true,
                genderChose = null,
                isEnableBtnCreate = false,
                isCreateSuccess = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == expectedUiState)
    }

    @Test
    fun onGenderChanged_Test() {
        mViewModel.onGenderChanged(Gender.MALE)
        val expectedUiState = ProfileSetupViewModel.UIWrapper(
                isFirstnameEmpty = true,
                isLastnamEmpty = true,
                birthdayNull = true,
                genderChose = Gender.MALE,
                isEnableBtnCreate = false,
                isCreateSuccess = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == expectedUiState)
    }

    @Test
    fun onDataUsageCheck() {
        mViewModel.onCbDataUsageClick(true)
        val expectedUiState = ProfileSetupViewModel.UIWrapper(
                isFirstnameEmpty = true,
                isLastnamEmpty = true,
                birthdayNull = true,
                genderChose = null,
                isEnableBtnCreate = false,
                isCreateSuccess = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == expectedUiState)
    }

    @Test
    fun onBirthdaySelect() {
        val calendar = Calendar.getInstance()
        calendar.set(1998,9,1)
        mViewModel.onBirthDaySelected(calendar)
        val expectedUiState = ProfileSetupViewModel.UIWrapper(
                isFirstnameEmpty = true,
                isLastnamEmpty = true,
                birthdayNull = false,
                genderChose = null,
                isEnableBtnCreate = false,
                isCreateSuccess = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == expectedUiState)
    }

    @Test
    fun createAccountWithEmail_Test() {

        Mockito.`when`(mUserDatabase.userDAO()).thenReturn(userDao)

        Whitebox.setInternalState(mViewModel,"mEmail","abc@gmail.com")
        Whitebox.setInternalState(mViewModel,"mPassword","abc1234")
        Whitebox.setInternalState(mViewModel,"mFirstName","phong")
        Whitebox.setInternalState(mViewModel,"mLastName","Truong")
        Whitebox.setInternalState(mViewModel,"mGender","M")
        Whitebox.setInternalState(mViewModel,"mBirthDay","01/09/1998")

        mViewModel.createAccountWithEmail()

        val expectedUiState = ProfileSetupViewModel.UIWrapper(
                isFirstnameEmpty = false,
                isLastnamEmpty = false,
                birthdayNull = false,
                genderChose = Gender.MALE,
                isEnableBtnCreate = true,
                isCreateSuccess = true
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.uiState)
        assert(uiState == expectedUiState)
    }



}