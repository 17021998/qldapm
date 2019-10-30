package com.example.hearthrate.Base

import android.util.Log
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun isActive(): Boolean {
        return isAdded
    }

    fun addFragment(fragment: Fragment, tag: String, frameId: Int) {
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(frameId, fragment, tag)
        transaction.commitAllowingStateLoss()
    }

    fun showLoadingDialog() {
        Log.d(TAG,"showLoadingDialog")
        if(isActive() && activity != null) {
            (activity as BaseActivity).showLoadingDialog()
        }
    }

    fun showLoadingDialog(title: String) {
        Log.d(TAG,"showLoadingDialog")
        if(isActive() && activity != null) {
            (activity as BaseActivity).showLoadingDialog(title)
        }
    }

    fun hideLoadingDialog() {
        Log.d(TAG,"hideLoadingDialog")
        if(activity != null) {
            (activity as BaseActivity).hideLoadingDialog()
        }
    }

    companion object {
        const val TAG = "BaseFragment"
    }
}