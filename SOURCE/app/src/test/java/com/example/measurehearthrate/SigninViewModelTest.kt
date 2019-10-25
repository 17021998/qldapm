package com.example.measurehearthrate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.measurehearthrate.Helper.DialogHelper
import com.example.measurehearthrate.Helper.ValidationHelper
import com.example.measurehearthrate.Model.User
import com.example.measurehearthrate.Usecase.SigninUsecase
import com.example.measurehearthrate.Utils.CoroutineUsecase
import com.example.measurehearthrate.Utils.LiveDataTestUtils
import com.example.measurehearthrate.Utils.ResponseCode
import com.example.measurehearthrate.Utils.TextUtils
import com.example.measurehearthrate.ViewModel.SigninViewModel
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
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@PrepareForTest(TextUtils::class,ValidationHelper::class)
@RunWith(PowerMockRunner::class)
class SigninViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mViewModel: SigninViewModel

    @Mock
    private lateinit var mSignInUsecase: SigninUsecase

    private lateinit var userResponse: User

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

    @Captor
    private lateinit var loginCaptor: ArgumentCaptor<CoroutineUsecase.UseCaseCallBack<SigninUsecase.ResponseValue, SigninUsecase.ErrorValue>>

    @Before
    fun start() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        mViewModel = SigninViewModel(mSignInUsecase)
        userResponse = User("abc@gmail.com","abc1234")
    }

    @After
    fun stop() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun onEmailTextChange_Email_Null() {
        mViewModel.onEmailTextChanged("")
        val expectedState = SigninViewModel.UiLoginWrapper(
                isLoginSuccess = null,
                enableChecked = false,
                isEnable = false,
                wrongEmail = false,
                wrongPassword = false
        )

        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(expectedState == uiState)
    }

    @Test
    fun onEmailTextChange_Email_Invalid() {
        mViewModel.onEmailTextChanged("abc")
        val expectedState = SigninViewModel.UiLoginWrapper(
                isLoginSuccess = null,
                enableChecked = false,
                isEnable = false,
                wrongEmail = false,
                wrongPassword = false
        )

        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(expectedState == uiState)
    }

    @Test
    fun onEmailTextChange_Email_valid() {
        mViewModel.onEmailTextChanged("abc@gmail.com")
        val expectedState = SigninViewModel.UiLoginWrapper(
                isLoginSuccess = null,
                enableChecked = true,
                isEnable = false,
                wrongEmail = false,
                wrongPassword = false
        )

        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(expectedState == uiState)
    }

    @Test
    fun login_success() {
        mViewModel.login("abc@gmail.com","abc123")
        var uiDialog = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(showDialog == uiDialog)
        Mockito.verify(mSignInUsecase).executeUsecase(anyOrNull(), loginCaptor.capture())
        loginCaptor.value.onSuccess(SigninUsecase.ResponseValue(userResponse))
        uiDialog = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(hideDialog == uiDialog)
        val expectedState = SigninViewModel.UiLoginWrapper(
                isLoginSuccess = true,
                enableChecked = false,
                isEnable = false,
                wrongEmail = false,
                wrongPassword = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(expectedState == uiState)

    }

    @Test
    fun login_failed_wrong_password() {
        mViewModel.login("abc@gmail.com","abc123")
        var uiDialog = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(showDialog == uiDialog)
        Mockito.verify(mSignInUsecase).executeUsecase(anyOrNull(), loginCaptor.capture())
        loginCaptor.value.onError(SigninUsecase.ErrorValue(ResponseCode.WRONG_PASSWORD,"wrongPassword"))
        uiDialog = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(hideDialog == uiDialog)
        val expectedState = SigninViewModel.UiLoginWrapper(
                isLoginSuccess = false,
                enableChecked = true,
                isEnable = true,
                wrongEmail = false,
                wrongPassword = true
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(expectedState == uiState)

    }

    @Test
    fun login_failed_wrong_email() {
        mViewModel.login("abc@gmail.com","abc123")
        var uiDialog = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(showDialog == uiDialog)
        Mockito.verify(mSignInUsecase).executeUsecase(anyOrNull(), loginCaptor.capture())
        loginCaptor.value.onError(SigninUsecase.ErrorValue(ResponseCode.WRONG_EMAIL,"wrongEmail"))
        uiDialog = LiveDataTestUtils.getValue(DialogHelper.dialogState)
        assert(hideDialog == uiDialog)
        val expectedState = SigninViewModel.UiLoginWrapper(
                isLoginSuccess = false,
                enableChecked = true,
                isEnable = true,
                wrongEmail = true,
                wrongPassword = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(expectedState == uiState)
    }

    @Test
    fun enableBtnSignIn_EmailValid_PasswordNotEmpty() {
        Whitebox.setInternalState(mViewModel,"mEmail","abc@gmail.com")
        Whitebox.setInternalState(mViewModel,"mPassword","abc1234")
        mViewModel.isEnableSignInButton()
        val expectedState = SigninViewModel.UiLoginWrapper(
                isLoginSuccess = null,
                enableChecked = true,
                isEnable = true,
                wrongEmail = false,
                wrongPassword = false
        )
        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(expectedState == uiState)
    }

    @Test
    fun enableBtnSignIn_EmailValid_PasswordEmpty() {
        Whitebox.setInternalState(mViewModel,"mEmail","abc@gmail.com")
        Whitebox.setInternalState(mViewModel,"mPassword","")
        mViewModel.isEnableSignInButton()
        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(uiState == null)
    }

    @Test
    fun enableBtnSignIn_EmailNotValid_PasswordNotEmpty() {
        Whitebox.setInternalState(mViewModel,"mEmail","abcgmail.com")
        Whitebox.setInternalState(mViewModel,"mPassword","abc1234")
        mViewModel.isEnableSignInButton()
        val uiState = LiveDataTestUtils.getValue(mViewModel.loginState)
        assert(uiState == null)
    }


}