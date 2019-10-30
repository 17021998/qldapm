package com.example.hearthrate.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.R

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