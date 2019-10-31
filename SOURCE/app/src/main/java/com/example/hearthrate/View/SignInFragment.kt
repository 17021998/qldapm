package com.example.hearthrate.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Base.BaseFragment
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.Dagger.Module.SignInModule
import com.example.hearthrate.Factory.AppViewModelFactory
import com.example.hearthrate.Helper.DialogHelper
import com.example.hearthrate.Helper.LanguagesHelper
import com.example.hearthrate.R
import com.example.hearthrate.Utils.AutoClearedValue
import com.example.hearthrate.Utils.DialogUtils
import com.example.hearthrate.ViewModel.SigninViewModel
import com.example.hearthrate.databinding.FragmentSigninBinding
import kotlinx.android.synthetic.main.fragment_signin.*
import javax.inject.Inject

class SignInFragment : BaseFragment() {

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private lateinit var mViewModel: SigninViewModel
    private lateinit var mBinding: AutoClearedValue<FragmentSigninBinding>

    companion object {
        fun newInstance() = SignInFragment()
        const val TAG = "SignUpFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val databinding = DataBindingUtil.inflate<FragmentSigninBinding>(inflater, R.layout.fragment_signin, container, false)

        MyApplication.instance.appComponent
                .plus(SignInModule())
                .inject(this)

        mViewModel = ViewModelProviders.of(this, appViewModelFactory).get(SigninViewModel::class.java)


        mBinding = AutoClearedValue(this, databinding)
        return mBinding.get()!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.get()?.let { binding ->

            binding.etEmail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mViewModel.onEmailTextChanged(s.toString())
                }

            })

            binding.etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    mViewModel.onPasswordTextChanged(s.toString())
                }
            })

            binding.tvDontHaveAccount.setOnClickListener {
                SignUpActivity.start(it.context)
            }

            binding.btnLogin.setOnClickListener {
                mViewModel.login(et_email.text.toString(), et_password.text.toString())
            }

            binding.ivGoogle.setOnClickListener {
                mViewModel.loginWithGoogle(activity as BaseActivity)
            }
        }



        viewModelObserve()
    }

    private fun viewModelObserve() {
        mViewModel.loginState.observe(this, Observer {
            val uiLoginModel = it ?: return@Observer

            mBinding.get()?.ivCheckedEmail?.let {
                it.visibility = if (uiLoginModel.enableChecked) View.VISIBLE else View.GONE
            }

            if (uiLoginModel.isEnable) {
                enableBtnSignIn()
            } else {
                disableBtnSignIn()
            }

            if (uiLoginModel.isLoginSuccess != null) {
                if (uiLoginModel.isLoginSuccess!!) {
                    activity?.let {
                        com.example.hearthrate.View.MainActivity.start(it)
                        it.finish()
                    }
                }
            }

            mBinding.get()?.let {
                it.inputEmail.isErrorEnabled = uiLoginModel.wrongEmail
                it.inputEmail.error = uiLoginModel.emailError
            }


            if(uiLoginModel.wrongPassword != null) {
                mBinding.get()?.let {
                    it.inputPassword.isErrorEnabled = uiLoginModel.wrongPassword!!
                    it.inputPassword.error = LanguagesHelper.getString(MyApplication.instance, R.string.Login_Text__PasswordIncorrect)
                }
            }

            uiLoginModel.serverError?.let {
                handleErrorCode(it)
            }
        })

        DialogHelper.dialogState.observe(this, Observer {
            val dialogModel = it ?: return@Observer

            dialogModel.isshowingDialog?.let {
                when (it) {
                    true -> showLoadingDialog()
                    else -> hideLoadingDialog()
                }
            }
        })
    }

    private fun handleErrorCode(errorCode: Int) {
        DialogUtils.showError(errorCode,childFragmentManager)
    }

    private fun disableBtnSignIn() {
        mBinding.get()?.let {
            it.btnLogin.isEnabled = false
            it.btnLogin.isClickable = false
            it.btnLogin.isFocusable = false
            it.btnLogin.setBackgroundColor(MyApplication.instance.resources.getColor(R.color.gray))

        }
    }

    private fun enableBtnSignIn() {
        mBinding.get()?.let {
            it.btnLogin.isEnabled = true
            it.btnLogin.isClickable = true
            it.btnLogin.isFocusable = true
            it.btnLogin.setBackgroundColor(MyApplication.instance.resources.getColor(R.color.primaryColor))

        }
    }


}