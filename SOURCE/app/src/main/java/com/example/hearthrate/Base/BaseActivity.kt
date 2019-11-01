package com.example.hearthrate.Base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hearthrate.R
import com.example.hearthrate.Utils.DialogUtils
import com.example.hearthrate.View.AlertDialogFragment
import com.example.hearthrate.View.DialogLoadingFragment
import kotlinx.android.synthetic.main.dialog_confirm.view.*

open class BaseActivity : AppCompatActivity(), AlertDialogFragment.OnDialogFragmentResult {


    protected val TAG = this.javaClass.simpleName

    private val mHandler = Handler()

    private var mDialogFragment: DialogLoadingFragment? = null

    protected fun addFragment(fragment: Fragment, tag: String?, frameId: Int) {
        supportFragmentManager.beginTransaction()
                .replace(frameId, fragment, tag)
                .addToBackStack(tag)
                .commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "BaseActivity onCreate")
        MyApplication.instance.appComponent.inject(this)
    }

    fun showLoadingDialog() {
        showLoadingDialog(false)
    }

    fun showLoadingDialog(title: String) {
        showLoadingDialog(false, title)
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

    override fun onDialogFragmentResult(tag: String, actionId: Int, data: Intent) {
        when (tag) {
            DialogUtils.NO_INTERNET_CONNECTION -> when (actionId) {
                R.id.tv_ok -> navigateToWifiSetting()
            }
        }
    }

    private fun navigateToWifiSetting() {
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }
}