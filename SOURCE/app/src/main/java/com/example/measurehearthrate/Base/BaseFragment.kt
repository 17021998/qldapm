package com.example.measurehearthrate.Base

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
}