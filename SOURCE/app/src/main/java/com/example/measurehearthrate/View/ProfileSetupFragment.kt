package com.example.measurehearthrate.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.measurehearthrate.Base.BaseFragment
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Dagger.Module.ProfileSetupModule
import com.example.measurehearthrate.Factory.AppViewModelFactory
import com.example.measurehearthrate.Model.User
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Utils.AutoClearedValue
import com.example.measurehearthrate.ViewModel.ProfileSetupViewModel
import com.example.measurehearthrate.databinding.FragmentProfileSetupBinding
import kotlinx.android.synthetic.main.fragment_profile_setup.*
import javax.inject.Inject

class ProfileSetupFragment: BaseFragment() {

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory

    private lateinit var mViewModel: ProfileSetupViewModel
    private lateinit var mBinding : AutoClearedValue<FragmentProfileSetupBinding>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var dataBinding = DataBindingUtil.inflate<FragmentProfileSetupBinding>(inflater, R.layout.fragment_profile_setup,container,false)

        MyApplication.instance.appComponent
                .plus(ProfileSetupModule())
                .inject(this)

        mViewModel = ViewModelProviders.of(this, appViewModelFactory).get(ProfileSetupViewModel::class.java)

        mBinding = AutoClearedValue(this,dataBinding)
        return mBinding.get()!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.get()?.let { binding ->
            binding.tvSkip.setOnClickListener {
                MainActivity.start(it.context)
            }
        }

    }

    companion object {
        const val ARG_PARAM = "New User"
        fun newInstance() = ProfileSetupFragment()
    }

}