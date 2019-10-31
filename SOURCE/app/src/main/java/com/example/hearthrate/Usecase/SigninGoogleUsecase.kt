package com.example.hearthrate.Usecase

import android.util.Log
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Helper.DialogHelper
import com.example.hearthrate.Interface.SocialLoginCallback
import com.example.hearthrate.Model.SocialAuth
import com.example.hearthrate.Utils.CoroutineUsecase
import com.example.hearthrate.Utils.ErrorCode
import com.google.android.gms.common.ConnectionResult
import java.lang.ref.WeakReference
import javax.inject.Inject

class SigninGoogleUsecase @Inject constructor(private var mSigninGoogleManager: SigninGoogleManager):
        CoroutineUsecase<SigninGoogleUsecase.RequestValue,SigninGoogleUsecase.ResponseValue,SigninGoogleUsecase.ResponseError>(){

    override suspend fun run(requestValues: RequestValue?): Any? = try {
        Log.d(TAG, "running Usecase")
        //if(MyApplication.instance.hasNetworkConnection()) {
            mSigninGoogleManager.loginWithGoogle(requestValues?.activity!!, object : SocialLoginCallback {
                override fun onLoginSucessed(mAuth: SocialAuth) {
                    Log.d(TAG, "onLoginSucessed")
                    DialogHelper.emitDialogState(false)
                }

                override fun onLoginFailed(errorCode: Int, connectionResult: ConnectionResult?) {
                    Log.d(TAG, "onLoginFailed")
                    DialogHelper.emitDialogState(false)
                }

            })

//        } else {
//            ResponseError(ErrorCode.NO_INTERNET_CONNECTION)
//        }
    } catch (exception: Exception) {
        Log.d(TAG, "Inside .run failed with exceprtion=$exception")
        ResponseError(ErrorCode.UNKNOWN_ERROR)
    }


    class RequestValue(val activity: WeakReference<BaseActivity>) : CoroutineUsecase.RequestValue

    class ResponseValue(val mAuth: SocialAuth) : CoroutineUsecase.ResponseValue

    class ResponseError(val errorCode: Int) : CoroutineUsecase.ErrorValue

    companion object {
        const val TAG = "SigninGoogleUsecase"
    }
}