package com.example.hearthrate.Interface

import com.example.hearthrate.Model.SocialAuth
import com.google.android.gms.common.ConnectionResult

interface SocialLoginCallback {

    fun onLoginSucessed(mAuth: SocialAuth)

    fun onLoginFailed(errorCode: Int, connectionResult: ConnectionResult?)

}