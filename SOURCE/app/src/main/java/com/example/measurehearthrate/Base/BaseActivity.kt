package com.example.measurehearthrate.Base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity : AppCompatActivity() {


    protected val TAG = this.javaClass.simpleName

    protected fun addFragment(fragment: Fragment, tag: String?, frameId: Int) {
        supportFragmentManager.beginTransaction()
                .replace(frameId, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"BaseActivity onCreate")
        MyApplication.instance.appComponent.inject(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}