package com.example.hearthrate.Utils

import android.util.Log
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MD5Algorithm {

    companion object {

        const val TAG = "MD5Algorithm"

        fun md5Encrypt(pass : String): String {
            var result: String = ""
            val digest: MessageDigest
            try {
                digest = MessageDigest.getInstance("MD5")
                digest.update(pass.toByteArray())
                val bigInteger = BigInteger(1,digest.digest())
                result = bigInteger.toString(16)
            } catch (e : NoSuchAlgorithmException) {
                Log.e(TAG,"$e")
            }
            return result
        }
    }

}