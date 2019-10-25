package com.example.measurehearthrate.Utils

object TextUtils {

    fun isEmpty(str: CharSequence?): Boolean {
        return str == null || str.length == 0
    }
}