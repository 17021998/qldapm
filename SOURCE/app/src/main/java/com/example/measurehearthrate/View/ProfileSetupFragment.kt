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
import com.example.measurehearthrate.Dagger.Module.ProfileSetupModule
import com.example.measurehearthrate.Factory.AppViewModelFactory
import com.example.measurehearthrate.Helper.ButtonHelper
import com.example.measurehearthrate.Model.User
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Utils.AutoClearedValue
import com.example.measurehearthrate.Utils.Gender
import com.example.measurehearthrate.ViewModel.ProfileSetupViewModel
import com.example.measurehearthrate.databinding.FragmentProfileSetupBinding
import kotlinx.android.synthetic.main.fragment_profile_setup.*
import javax.inject.Inject
import android.app.DatePickerDialog
import android.util.Log
import com.example.measurehearthrate.Helper.DialogHelper
import java.util.*


class ProfileSetupFragment : BaseFragment() {

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private lateinit var mViewModel: ProfileSetupViewModel
    private lateinit var mBinding: AutoClearedValue<FragmentProfileSetupBinding>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var dataBinding = DataBindingUtil.inflate<FragmentProfileSetupBinding>(inflater, R.layout.fragment_profile_setup, container, false)

        MyApplication.instance.appComponent
                .plus(ProfileSetupModule())
                .inject(this)

        mViewModel = ViewModelProviders.of(this, appViewModelFactory).get(ProfileSetupViewModel::class.java)

        viewModelObserve()

        mBinding = AutoClearedValue(this, dataBinding)
        return mBinding.get()!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val user = arguments!!.getParcelable<User>("user")
            mViewModel.receiveEmailPassword(user.mEmail,user.mPassword)
            et_email.text = Editable.Factory.getInstance().newEditable(user.mEmail)
        }

        mBinding.get()?.let { binding ->

            binding.etFirstName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mViewModel.onFirstNameChanged(s.toString())
                }
            })

            binding.etLastName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mViewModel.onLastNameChanged(s.toString())
                }

            })

            binding.etBirthday.setOnClickListener {
                val calendar = Calendar.getInstance()
                val mDay = calendar.get(Calendar.DAY_OF_MONTH)
                val mMonth = calendar.get(Calendar.MONTH) +1
                val mYear = calendar.get(Calendar.YEAR)
                val datePickerDialog = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    calendar.set(year,month,dayOfMonth)
                    mViewModel.onBirthDaySelected(calendar)
                }, mYear, mMonth, mDay)
                datePickerDialog.show()

            }

            binding.btnMale.setOnClickListener {
                mViewModel.onGenderChanged(Gender.MALE)
            }

            binding.btnFemale.setOnClickListener {
                mViewModel.onGenderChanged(Gender.FEMALE)
            }

            binding.btnOther.setOnClickListener {
                mViewModel.onGenderChanged(Gender.OTHER)
            }


            binding.cbCollectData.setOnCheckedChangeListener { _, isChecked ->
                mViewModel.onCbDataUsageClick(isChecked)
            }

            binding.btnCreateAccount.setOnClickListener {
                mViewModel.createAccountWithEmail()
            }

            binding.ivClose.setOnClickListener {
                activity?.finish()
            }

        }

    }

    private fun viewModelObserve() {
        mViewModel.uiState.observe(this, Observer {
            val uiModel = it ?: return@Observer

            Log.d("abc","$uiModel")

            mBinding.get()?.ivCheckedFirstName?.let {
                it.visibility = if (!uiModel.isFirstnameEmpty) View.VISIBLE else View.GONE
            }

            mBinding.get()?.ivCheckedLastName?.let {
                it.visibility = if (!uiModel.isLastnamEmpty) View.VISIBLE else View.GONE
            }

            if (uiModel.genderChose != null) {
                updateGender(uiModel.genderChose!!)
            }

            if(!uiModel.birthdayNull) {
                mBinding.get()?.etBirthday?.setText(mViewModel.getBirthDay())
            }

            if(uiModel.isEnableBtnCreate) {
                ButtonHelper.enableButton(mBinding.get()?.btnCreateAccount)
            } else {
                ButtonHelper.disableButton(mBinding.get()?.btnCreateAccount)
            }

            if (uiModel.isCreateSuccess) {
                activity?.let {
                    MainActivity.start(it)
                    it.finish()
                }
            }
        })

        DialogHelper.dialogState.observe(this, Observer {
            val dialogModel = it?:return@Observer

            dialogModel.isshowingDialog?.let {
                when(it) {
                    true -> showLoadingDialog()
                    else -> hideLoadingDialog()
                }
            }
        })
    }

    private fun updateGender(gender: Gender) {

        val disableTextColor = MyApplication.instance.getColor(R.color.primaryColor)
        val activeTextColor = MyApplication.instance.getColor(R.color.colorWhite)
        val disableBackground = MyApplication.instance.getDrawable(R.drawable.background_disable)
        val activeBackground = MyApplication.instance.getDrawable(R.drawable.background_active)

        mBinding.get()?.let {

            it.btnMale.background = disableBackground
            it.btnMale.setTextColor(disableTextColor)
            it.btnFemale.background = disableBackground
            it.btnFemale.setTextColor(disableTextColor)
            it.btnOther.background = disableBackground
            it.btnOther.setTextColor(disableTextColor)

            when (gender) {
                Gender.MALE -> {
                    it.btnMale.background = activeBackground
                    it.btnMale.setTextColor(activeTextColor)
                }
                Gender.FEMALE -> {
                    it.btnFemale.background = activeBackground
                    it.btnFemale.setTextColor(activeTextColor)
                }
                Gender.OTHER -> {
                    it.btnOther.background = activeBackground
                    it.btnOther.setTextColor(activeTextColor)
                }
            }
        }
    }

    companion object {
        fun newInstance(user: User): ProfileSetupFragment {
            val fragment = ProfileSetupFragment()
            val args = Bundle()
            args.putParcelable("user",user)
            fragment.arguments = args
            return fragment
        }
    }

}