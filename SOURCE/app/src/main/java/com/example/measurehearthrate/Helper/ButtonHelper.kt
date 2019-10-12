package com.example.measurehearthrate.Helper

import android.widget.Button
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.R

class ButtonHelper {
    companion object {
        fun enableButton(button: Button?) {
            button?.let {
                it.isFocusable = true
                it.isEnabled = true
                it.isClickable = true
                it.setBackgroundColor(MyApplication.instance.getColor(R.color.primaryColor))
            }
        }

        fun disableButton(button: Button?) {
            button?.let {
                it.isFocusable = false
                it.isEnabled = false
                it.isClickable = false
                it.setBackgroundColor(MyApplication.instance.getColor(R.color.gray))
            }
        }
    }
}