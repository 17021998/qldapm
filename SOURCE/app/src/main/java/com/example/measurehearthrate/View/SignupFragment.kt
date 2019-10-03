package com.example.measurehearthrate.View

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.measurehearthrate.Base.BaseFragment
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Dagger.Module.SignUpModule
import com.example.measurehearthrate.Factory.AppViewModelFactory
import com.example.measurehearthrate.Interface.SignupContracts
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Utils.AutoClearedValue
import com.example.measurehearthrate.ViewModel.SignUpViewModel
import com.example.measurehearthrate.databinding.FragmentSignupBinding
import javax.inject.Inject

class SignUpFragment : BaseFragment(), SignupContracts {


    lateinit var mViewModel: SignUpViewModel
    private lateinit var mBinding: AutoClearedValue<FragmentSignupBinding>

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentSignupBinding>(inflater, R.layout.fragment_signup, container, false)

//        MyApplication.appComponent
//                .plus(SignUpModule(this))
//                .inject(this@SignUpFragment)

        mViewModel = ViewModelProviders.of(this@SignUpFragment,viewModelFactory).get(SignUpViewModel::class.java)

        ViewModelObserve()

        mBinding = AutoClearedValue(this, dataBinding)
        return mBinding.get()!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.get()?.let { binding ->
            binding.btnSignup.setOnClickListener { mViewModel.register() }

            binding.etEmail.addTextChangedListener(object :TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mViewModel.onEmailTextChanged(s.toString())
                }

            })

        }
    }


    private fun ViewModelObserve() {
        mViewModel.uiState.observe(this, Observer {
            val uiModel = it ?: return@Observer

            if (uiModel.register) {
                Toast.makeText(activity, "onClick BtnSignUp", Toast.LENGTH_SHORT).show()
            }
        })
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

    override fun onEmailValidation(isMet: Boolean, enableError: Boolean, errorMessage: String) {
        if (!isAdded) return
        mBinding.get()?.let {
            it.inputEmail.isErrorEnabled = enableError
            it.inputEmail.error = errorMessage
        }

        mBinding.get()?.ivCheckedEmail?.let {
            it.visibility = if (isMet) View.VISIBLE else View.GONE
        }

    }

    override fun onPasswordValidation(isMinCharacterMet: Boolean, isCombineLetterDigitMet: Boolean) {
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
    }


}