package com.example.measurehearthrate.Helper

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import com.example.measurehearthrate.R

class LanguagesHelper {

    companion object {
        fun getString(context: Context?, resourceId: Int): String {
            if (context == null || resourceId == 0) {
                return ""
            }

            val resourceName: String

            try {
                resourceName = context.resources.getString(resourceId)
            } catch (e: Resources.NotFoundException) {
                return ""
            }

            if (TextUtils.isEmpty(resourceName)) {
                return ""
            }


            return resourceName
        }
    }

}