package com.example.measurehearthrate.View

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.measurehearthrate.Base.BaseFragment
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Dagger.Module.SignUpModule
import com.example.measurehearthrate.Factory.AppViewModelFactory
import com.example.measurehearthrate.Interface.SignupContracts
import com.example.measurehearthrate.Model.User
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Utils.AutoClearedValue
import com.example.measurehearthrate.Utils.MD5Algorithm
import com.example.measurehearthrate.ViewModel.SignUpViewModel
import com.example.measurehearthrate.databinding.FragmentSignupBinding
import javax.inject.Inject

class SignUpFragment : BaseFragment(), SignupContracts {

    override fun register() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var mViewModel: SignUpViewModel
    private lateinit var mBinding: AutoClearedValue<FragmentSignupBinding>

    var email : String = ""
    var pass : String = ""

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentSignupBinding>(inflater, R.layout.fragment_signup, container, false)

        MyApplication.instance.appComponent
                .plus(SignUpModule())
                .inject(this)

        mViewModel = ViewModelProviders.of(this@SignUpFragment, viewModelFactory).get(SignUpViewModel::class.java)

        viewModelObserve()

        mBinding = AutoClearedValue(this, dataBinding)
        return mBinding.get()!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mBinding.get()?.let { binding ->

            binding.btnSignup.setOnClickListener {
                email = binding.etEmail.text.toString()
                pass = binding.etPassword.text.toString()
                mViewModel.register(email,pass) }


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
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mViewModel.onPasswordChanged(s.toString())
                }

            })

            binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
                mViewModel.onPasswordChanged()
                if (hasFocus) {
                    binding.tvErrorCheckCharacter.visibility = View.VISIBLE
                    binding.tvErrorCheckCombine.visibility = View.VISIBLE
                } else {
                    binding.tvErrorCheckCharacter.visibility = View.GONE
                    binding.tvErrorCheckCombine.visibility = View.GONE
                }
            }

            binding.ivClose.setOnClickListener {
                activity?.finish()
            }

            binding.tvHaveAccount.setOnClickListener {
                SignInActivity.start(it.context)
                activity?.finish()
            }


        }
    }

    private fun viewModelObserve() {
        mViewModel.passwordState.observe(this, Observer {
            val passModel = it?:return@Observer
            mBinding.get()?.let {
                if (passModel.isMinPasswordMet) {
                    it.tvErrorCheckCharacter.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MyApplication.instance,
                            R.drawable.ic_ok_button), null, null, null)
                } else {
                    it.tvErrorCheckCharacter.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MyApplication.instance,
                            R.drawable.gray_dot), null, null, null)
                }

                if (passModel.isPasswordFormatMet) {
                    it.tvErrorCheckCombine.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MyApplication.instance,
                            R.drawable.ic_ok_button), null, null, null)
                } else {
                    it.tvErrorCheckCombine.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MyApplication.instance,
                            R.drawable.gray_dot), null, null, null)
                }
            }
        })

        mViewModel.emailState.observe(this, Observer {
            val emailModel = it ?: return@Observer

            mBinding.get()?.let {
                it.inputEmail.isErrorEnabled = emailModel.enableError
                it.inputEmail.error = emailModel.errorMessage
            }

            mBinding.get()?.ivCheckedEmail?.let {
                it.visibility = if (emailModel.isValid!!) View.VISIBLE else View.GONE
            }

        })

        mViewModel.btnSignUpState.observe(this, Observer {
            val btnSignUpModel = it?: return@Observer
            if (btnSignUpModel.isSignUpSuccess) {
                Toast.makeText(activity,"SignUp Sucessful",Toast.LENGTH_SHORT).show()
                val encryptedPass = MD5Algorithm.md5Encrypt(pass)
                val user = User(email,encryptedPass)
                activity?.let {
                    ProfileSetupActivity.start(it,user)
                    it.finish()
                }
            }

            if(btnSignUpModel.isEnable) {

                enableSignUpButton()
            }
            else {
                disableSignUpButton()
            }
        })


    }

    private fun enableSignUpButton() {
        mBinding.get()?.let {
            it.btnSignup.isEnabled = true
            it.btnSignup.isClickable = true
            it.btnSignup.isFocusable = true
            it.btnSignup.setBackgroundColor(MyApplication.instance.getColor(R.color.primaryColor))
        }
    }

    private fun disableSignUpButton() {
        mBinding.get()?.let {
            it.btnSignup.isEnabled = false
            it.btnSignup.isClickable = false
            it.btnSignup.isFocusable = false
            it.btnSignup.setBackgroundColor(MyApplication.instance.resources.getColor(R.color.gray))

        }
    }

    override fun enableSignup() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableSignup() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateHome() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.start()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        fun newInstance() = SignUpFragment()
        const val TAG = "SignUpFragment"
    }


}