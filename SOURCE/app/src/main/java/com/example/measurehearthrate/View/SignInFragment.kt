package com.example.measurehearthrate.View

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.measurehearthrate.Base.BaseFragment
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Dagger.Module.SignInModule
import com.example.measurehearthrate.Factory.AppViewModelFactory
import com.example.measurehearthrate.Helper.DialogHelper
import com.example.measurehearthrate.Helper.LanguagesHelper
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Utils.AutoClearedValue
import com.example.measurehearthrate.ViewModel.SigninViewModel
import com.example.measurehearthrate.databinding.FragmentSigninBinding
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
        }



        viewModelObserve()
    }

    private fun viewModelObserve() {
        mViewModel.loginState.observe(this, Observer {
            val btnLoginModel = it ?: return@Observer

            mBinding.get()?.ivCheckedEmail?.let {
                it.visibility = if (btnLoginModel.enableChecked) View.VISIBLE else View.GONE
            }

            if (btnLoginModel.isEnable) {
                enableBtnSignIn()
            } else {
                disableBtnSignIn()
            }

            if (btnLoginModel.isLoginSuccess != null) {
                if (btnLoginModel.isLoginSuccess!!) {
                    activity?.let {
                        MainActivity.start(it)
                        it.finish()
                    }
                }
            }

            mBinding.get()?.let {
                it.inputEmail.isErrorEnabled = btnLoginModel.wrongEmail
                it.inputEmail.error = btnLoginModel.emailError
            }


            if(btnLoginModel.wrongPassword != null) {
                mBinding.get()?.let {
                    it.inputPassword.isErrorEnabled = btnLoginModel.wrongPassword!!
                    it.inputPassword.error = LanguagesHelper.getString(MyApplication.instance, R.string.Login_Text__PasswordIncorrect)
                }
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