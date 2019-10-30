package com.example.hearthrate.Interface

import com.example.hearthrate.Model.SocialAuth

interface SocialLoginCallback {

    fun onLoginSucessed(mAuth: SocialAuth)

    fun onLoginFailed(errorCode: Int)

}