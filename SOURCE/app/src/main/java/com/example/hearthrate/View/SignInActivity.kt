package com.example.hearthrate.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Interface.SignInContracts
import com.example.hearthrate.R
import com.example.hearthrate.Usecase.SigninGoogleManager
import javax.inject.Inject

class SignInActivity : BaseActivity(), SignInContracts {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, SignInActivity::class.java))
        }
    }

    @Inject
    lateinit var mSigninGoogleManager: SigninGoogleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        mSigninGoogleManager = SigninGoogleManager()

        var fragment = supportFragmentManager.findFragmentById(R.id.content) as SignInFragment?

        if(fragment == null) {
            fragment = SignInFragment.newInstance()
            addFragment(fragment,TAG,R.id.content)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null) {
            mSigninGoogleManager.onActivityResult(requestCode,resultCode,data)
            supportFragmentManager.findFragmentById(R.id.content)?.onActivityResult(requestCode, resultCode, data)
        }
    }
}