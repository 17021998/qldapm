package com.example.hearthrate.Base

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hearthrate.View.DialogLoadingFragment

open class BaseActivity : AppCompatActivity() {


    protected val TAG = this.javaClass.simpleName

    private val mHandler = Handler()

    private var mDialogFragment: DialogLoadingFragment?= null

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

    fun showLoadingDialog() {
        showLoadingDialog(false)
    }

    fun showLoadingDialog(title: String) {
        showLoadingDialog(false,title)
    }

    private fun showLoadingDialog(cancelable: Boolean, title: String = "") {
        Log.d(TAG, "showLoadingDialog: cancelable = $cancelable")
        hideLoadingDialog()
        mHandler.post(Runnable {
            try {
                if (isDestroyed || isFinishing) {
                    Log.d(TAG, "Activity is destroy or finishing, no need to show dialog")
                    return@Runnable
                }

                mDialogFragment = DialogLoadingFragment.newInstance(title)
                mDialogFragment!!.isCancelable = cancelable
                val ft = supportFragmentManager.beginTransaction()
                ft.add(mDialogFragment!!, DialogLoadingFragment.TAG)
                ft.commitAllowingStateLoss()
            } catch (e: Exception) {
                Log.d(TAG, "Exception when showing progress dialog=$e")
            }
        })
    }

    fun hideLoadingDialog() {
        Log.d(TAG, "hideLoadingDialog")
        mHandler.post {
            try {
                if (mDialogFragment == null) {
                    val foundDialog = supportFragmentManager.findFragmentByTag(DialogLoadingFragment.TAG)

                    foundDialog?.let {
                        mDialogFragment = foundDialog as DialogLoadingFragment
                    }
                }
                if (mDialogFragment != null) {
                    Log.d(TAG, "hideLoadingDialog dismissAllowingStateLoss")
                    mDialogFragment!!.dismissAllowingStateLoss()
                    mDialogFragment = null
                }
            } catch (e: Exception) {
                Log.d(TAG, "Exception when dismiss progress dialog=$e")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}