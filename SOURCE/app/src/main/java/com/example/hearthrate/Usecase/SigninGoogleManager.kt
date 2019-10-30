package com.example.hearthrate.Usecase

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.Interface.SocialLoginCallback
import com.example.hearthrate.Model.SocialAuth
import com.example.hearthrate.Utils.ErrorCode
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import java.lang.ref.WeakReference
import javax.inject.Inject

class SigninGoogleManager @Inject constructor() {

    private lateinit var gso: GoogleSignInOptions
    private var mGoogleSignInClient : GoogleSignInClient? = null
    private var callback: SocialLoginCallback? = null
    private var weakFragmentActivity: WeakReference<BaseActivity>? = null

    fun loginWithGoogle(activity: WeakReference<BaseActivity>,callBack: SocialLoginCallback) {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(MyApplication.instance.applicationContext, gso)
        this.callback = callback
        this.weakFragmentActivity = activity
        disconnectGoogleConnection()
        connectNewAccount()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        Log.d(TAG, "Inside .onActivityResult requestCode=$requestCode, resultCode=$resultCode")
        if (requestCode == GOOGLE_SIGN_IN) {
            if (data != null) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                handleSignInResult(result)
                Log.d(TAG, "Inside .onActivityResult googleSignInResult=" + result.status)
            } else {
                handleSignInResult(null)
            }
        }
        return true
    }

    private fun handleSignInResult(result: GoogleSignInResult?) {
        if(result == null) {
            disconnectGoogleConnection()
            callback?.onLoginFailed(ErrorCode.UNKNOWN_ERROR)
        } else {
            if(result.isSuccess) {
                val account = result.signInAccount
                if(account == null) {
                    callback?.onLoginFailed(ErrorCode.UNKNOWN_ERROR)
                } else {
                    val ggAuth = SocialAuth()
                    ggAuth.email = account.email ?: ""
                    ggAuth.firstName = account.givenName ?: ""
                    ggAuth.lastName = account.familyName ?: ""
                    ggAuth.id = account.id ?: ""
                    ggAuth.token = account.idToken ?: ""
                    callback?.onLoginSucessed(ggAuth)
                    disconnectGoogleConnection()
                }

            }
        }

    }


    private fun connectNewAccount() {
        Log.d(TAG, "connectNewAccount")
        if (weakFragmentActivity != null && weakFragmentActivity!!.get() != null && mGoogleSignInClient != null) {
            val signInIntent = mGoogleSignInClient!!.signInIntent
            weakFragmentActivity!!.get()?.startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
        } else {
            callback?.onLoginFailed(ErrorCode.UNKNOWN_ERROR)
        }
    }

    private fun disconnectGoogleConnection() {
        logOut(weakFragmentActivity as WeakReference<Activity>?)
    }

    fun logOut(weakActivity: WeakReference<Activity>? = null) {
        if (weakActivity?.get() != null && GoogleSignIn.getLastSignedInAccount(weakActivity.get()) != null) {
            Log.d(TAG, "inside .logOut(), googleSignInClient=$mGoogleSignInClient")
            if (mGoogleSignInClient != null) {
                val signOutTask = mGoogleSignInClient!!.signOut()
                signOutTask?.addOnCompleteListener {
                    Log.d(TAG, "Log out google account completely")
                }
                signOutTask?.addOnFailureListener {
                    Log.d(TAG, "Could not log out google account, error = ${it.localizedMessage}")
                    it.printStackTrace()
                }
            }
        }
    }

    companion object {
        const val TAG = "SigninGoogleManager"
        const val GOOGLE_SIGN_IN = 999
    }

}