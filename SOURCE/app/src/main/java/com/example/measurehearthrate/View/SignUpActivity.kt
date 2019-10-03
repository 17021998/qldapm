package com.example.measurehearthrate.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.measurehearthrate.Base.BaseActivity
import com.example.measurehearthrate.R
import dagger.android.AndroidInjection

class SignUpActivity : BaseActivity() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, SignUpActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        var fragment = supportFragmentManager.findFragmentById(R.id.content) as SignUpFragment?

        if(fragment == null) {
            fragment = SignUpFragment.newInstance()
            addFragment(fragment,TAG,R.id.content)
        }




    }
}