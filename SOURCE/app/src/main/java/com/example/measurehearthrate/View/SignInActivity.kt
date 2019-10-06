package com.example.measurehearthrate.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.measurehearthrate.Base.BaseActivity
import com.example.measurehearthrate.Interface.SignInContracts
import com.example.measurehearthrate.R

class SignInActivity : BaseActivity(), SignInContracts {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, SignInActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        var fragment = supportFragmentManager.findFragmentById(R.id.content)

        if(fragment == null) {
            fragment = SignInFragment.newInstance()
            addFragment(fragment,TAG,R.id.content)
        }
    }
}