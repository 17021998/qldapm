package com.example.hearthrate.Helper

import android.widget.Button
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.R

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