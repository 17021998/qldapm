package com.example.hearthrate.Helper

import androidx.core.util.PatternsCompat
import com.example.hearthrate.Utils.TextUtils
import java.util.regex.Pattern

class ValidationHelper {
    companion object {

        const val MINIMUM_PASSWORD_LENGTH = 7
        val passWordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-zA-Z]).+)")!!

        fun isEmailValid(email : CharSequence?) : Boolean{
            return !TextUtils.isEmpty(email) && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordFormatdValid(password: CharSequence?) : Boolean {
            return !TextUtils.isEmpty(password) && passWordPattern.matcher(password).matches()
        }

        fun isMinPasswordVilid(password: CharSequence?) : Boolean {
            return !TextUtils.isEmpty(password) && password.toString().length >= MINIMUM_PASSWORD_LENGTH
        }
    }


}