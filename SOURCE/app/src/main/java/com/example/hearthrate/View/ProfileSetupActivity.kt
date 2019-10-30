package com.example.hearthrate.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Model.User
import com.example.hearthrate.R

class ProfileSetupActivity: BaseActivity() {



    companion object {

        @JvmStatic
        fun start(context: Context, user: User) {
            val intent = Intent(context,ProfileSetupActivity::class.java)
            intent.putExtra("user",user)
            context.startActivity(intent)
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        //val emailUser = "asdasdaas@gasdas.cm"

        val user = intent.getParcelableExtra<User>("user")

        var fragment = supportFragmentManager.findFragmentById(R.id.content) as ProfileSetupFragment?

        if(fragment == null) {
            fragment = ProfileSetupFragment.newInstance(user)
            addFragment(fragment,TAG,R.id.content)
        }

    }
}