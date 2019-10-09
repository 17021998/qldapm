package com.example.measurehearthrate.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.measurehearthrate.Base.BaseActivity
import com.example.measurehearthrate.R

class ProfileSetupActivity: BaseActivity() {

    companion object {

        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context,ProfileSetupActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        var fragment = supportFragmentManager.findFragmentById(R.id.content) as ProfileSetupFragment?

        if(fragment == null) {
            fragment = ProfileSetupFragment.newInstance()
            addFragment(fragment,TAG,R.id.content)
        }

    }
}