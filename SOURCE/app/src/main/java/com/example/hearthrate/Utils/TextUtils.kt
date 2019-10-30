package com.example.hearthrate.Utils

object TextUtils {

    fun isEmpty(str: CharSequence?): Boolean {
        return str == null || str.length == 0
    }
}