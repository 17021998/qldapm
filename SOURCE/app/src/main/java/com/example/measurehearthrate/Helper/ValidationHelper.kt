package com.example.measurehearthrate.Helper

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

class ValidationHelper {
    companion object {
        fun isEmailValid(email : CharSequence) : Boolean{
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordValid(password: CharSequence) : Boolean {
            val pattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z]).{7,20})")
            return !TextUtils.isEmpty(password) && pattern.matcher(password).matches()
        }
    }
}