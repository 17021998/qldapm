package com.example.measurehearthrate.Helper

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

class ValidationHelper {
    companion object {

        const val MINIMUM_PASSWORD_LENGTH = 7
        val passWordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z]).+)")!!

        fun isEmailValid(email : CharSequence) : Boolean{
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordFormatdValid(password: CharSequence) : Boolean {
            return !TextUtils.isEmpty(password) && passWordPattern.matcher(password).matches()
        }

        fun isMinPasswordVilid(password: CharSequence) : Boolean {
            return !TextUtils.isEmpty(password) && password.toString().length >= MINIMUM_PASSWORD_LENGTH
        }
    }
}