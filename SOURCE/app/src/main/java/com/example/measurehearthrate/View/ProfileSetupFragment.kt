package com.example.measurehearthrate.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.measurehearthrate.Base.BaseFragment
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Utils.AutoClearedValue
import com.example.measurehearthrate.databinding.FragmentProfileSetupBinding

class ProfileSetupFragment: BaseFragment() {

    private lateinit var mBinding : AutoClearedValue<FragmentProfileSetupBinding>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var dataBinding = DataBindingUtil.inflate<FragmentProfileSetupBinding>(inflater, R.layout.fragment_profile_setup,container,false)

        mBinding = AutoClearedValue(this,dataBinding)
        return mBinding.get()!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        fun newInstance() = ProfileSetupFragment()
    }

}