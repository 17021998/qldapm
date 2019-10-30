package com.example.hearthrate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.Helper.AuthenticationEmailExistingHelper
import com.example.hearthrate.Helper.DialogHelper
import com.example.hearthrate.Utils.CoroutineUsecase
import com.example.hearthrate.Utils.LiveDataTestUtils
import com.example.hearthrate.ViewModel.SignUpViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
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
import org.mockito.*
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox


@RunWith(PowerMockRunner::class)
class SignupViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mAuthEmailExisting: AuthenticationEmailExistingHelper

    @Mock
    private lateinit var mViewModel: SignUpViewModel

    @Mock
    private lateinit var mApp: MyApplication

    @Captor
    private lateinit var registerCaptor: ArgumentCaptor<CoroutineUsecase.UseCaseCallBack<AuthenticationEmailExistingHelper.ResponseValue, AuthenticationEmailExistingHelper.ResponseError>>

    @ObsoleteCoroutinesApi
    private val mainThread = newSingleThreadContext("UI Thread")

    companion object {
        val showDialog = DialogHelper.DialogWrapper(
                isshowingDialog = true
        )

        val hideDialog = DialogHelper.DialogWrapper(
                isshowingDialog = false
        )
    }

    @Before
    fun start() {
        Dispatchers.setMain(mainThread)
        MockitoAnnotations.initMocks(this)
        mViewModel = SignUpViewModel(mAuthEmailExisting)
        Whitebox.setInternalState(MyApplication::class.java, "instance", mApp)

    }

    @After
    fun stop() {
        Dispatchers.resetMain()
        mainThread.close()
    }

    @Test
    fun onEmailTextChanged_EmailNull() {
        mViewModel.onEmailTextChanged("")
        val expectedEmailState = SignUpViewModel.UiEmailWrapper(
                isValid = false,
                enableError = false,
                errorMessage = ""
        )

        val emailState = LiveDataTestUtils.getValue(mViewModel.emailState)
        assert(expectedEmailState == emailState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = false,
                isEnable = false
        )
        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)
    }

    @Test
    fun onEmailTextChanged_EmailNotValid() {

        doReturn("Invalid email address")
                .`when`(mApp)
                .getString(any())

        mViewModel.onEmailTextChanged("abc")
        val expectedEmailState = SignUpViewModel.UiEmailWrapper(
                isValid = false,
                enableError = true,
                errorMessage = "Invalid email address"
        )

        val emailState = LiveDataTestUtils.getValue(mViewModel.emailState)
        assert(expectedEmailState == emailState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = false,
                isEnable = false
        )
        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)
    }

    @Test
    fun onEmailTextChanged_EmailValid() {

        mViewModel.onEmailTextChanged("abc@gmail.com")
        val expectedEmailState = SignUpViewModel.UiEmailWrapper(
                isValid = true,
                enableError = false,
                errorMessage = ""
        )

        val emailState = LiveDataTestUtils.getValue(mViewModel.emailState)
        assert(expectedEmailState == emailState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = false,
                isEnable = false
        )
        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)
    }

    @Test
    fun onPasswordChanged_PasswordNull() {
        mViewModel.onPasswordChanged("")
        val expectedPasswordState = SignUpViewModel.UiPassWordWrapper(
                isMinPasswordMet = false,
                isPasswordFormatMet = false
        )

        val emailState = LiveDataTestUtils.getValue(mViewModel.passwordState)
        assert(expectedPasswordState == emailState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = false,
                isEnable = false
        )
        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)
    }

    @Test
    fun onPasswordChanged_SizeLess7_FormatNotMet() {
        mViewModel.onPasswordChanged("assdas")
        val expectedPasswordState = SignUpViewModel.UiPassWordWrapper(
                isMinPasswordMet = false,
                isPasswordFormatMet = false
        )

        val emailState = LiveDataTestUtils.getValue(mViewModel.passwordState)
        assert(expectedPasswordState == emailState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = false,
                isEnable = false
        )
        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)
    }

    @Test
    fun onPasswordChanged_SizeMet_FormatNotMet() {
        mViewModel.onPasswordChanged("assdasdas")
        val expectedPasswordState = SignUpViewModel.UiPassWordWrapper(
                isMinPasswordMet = true,
                isPasswordFormatMet = false
        )

        val emailState = LiveDataTestUtils.getValue(mViewModel.passwordState)
        assert(expectedPasswordState == emailState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = false,
                isEnable = false
        )
        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)
    }

    @Test
    fun onPasswordChanged_SizeMet_FormatMet() {
        mViewModel.onPasswordChanged("assdasdas123")
        val expectedPasswordState = SignUpViewModel.UiPassWordWrapper(
                isMinPasswordMet = true,
                isPasswordFormatMet = true
        )

        val emailState = LiveDataTestUtils.getValue(mViewModel.passwordState)
        assert(expectedPasswordState == emailState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = false,
                isEnable = false
        )
        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)
    }

    @Test
    fun register_success() {
        mViewModel.register("abc@gmail.com", "abc1234")
        var dialogState = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(showDialog == dialogState)

        Mockito.verify(mAuthEmailExisting).executeUsecase(anyOrNull(), registerCaptor.capture())
        registerCaptor.value.onSuccess(AuthenticationEmailExistingHelper.ResponseValue(false))
        dialogState = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(hideDialog == dialogState)

        val expectedButtonState = SignUpViewModel.UiSignUpButtonWrapper(
                isSignUpSuccess = true,
                isEnable = true
        )

        val btnState = LiveDataTestUtils.getValue(mViewModel.btnSignUpState)
        assert(expectedButtonState == btnState)

    }

    @Test
    fun register_EmailExisted() {
        doReturn("This email is already in use")
                .`when`(mApp)
                .getString(any())
        mViewModel.register("abc@gmail.com", "abc1234")
        var dialogState = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(showDialog == dialogState)

        Mockito.verify(mAuthEmailExisting).executeUsecase(anyOrNull(), registerCaptor.capture())
        registerCaptor.value.onSuccess(AuthenticationEmailExistingHelper.ResponseValue(true))
        dialogState = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(hideDialog == dialogState)

        val expectedEmailState = SignUpViewModel.UiEmailWrapper(
                isValid = true,
                enableError = true,
                errorMessage = "This email is already in use"
                )

        val emailState = LiveDataTestUtils.getValue(mViewModel.emailState)
        assert(expectedEmailState == emailState)

    }


}