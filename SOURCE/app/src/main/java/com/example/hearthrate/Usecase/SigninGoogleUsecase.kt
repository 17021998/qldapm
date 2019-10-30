package com.example.hearthrate.Usecase

import android.util.Log
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Interface.SocialLoginCallback
import com.example.hearthrate.Model.SocialAuth
import com.example.hearthrate.Utils.CoroutineUsecase
import com.example.hearthrate.Utils.ErrorCode
import java.lang.ref.WeakReference
import javax.inject.Inject

class SigninGoogleUsecase @Inject constructor(private var mSigninGoogleManager: SigninGoogleManager):
        CoroutineUsecase<SigninGoogleUsecase.RequestValue,SigninGoogleUsecase.ResponseValue,SigninGoogleUsecase.ResponseError>(){

    override suspend fun run(requestValues: RequestValue?): Any? = try {
        Log.d(TAG, "running Usecase")
        //if(MyApplication.instance.hasNetworkConnection()) {
            mSigninGoogleManager.loginWithGoogle(requestValues?.activityContext!!, object : SocialLoginCallback {
                override fun onLoginSucessed(mAuth: SocialAuth) {
                    Log.d(TAG, "onLoginSucessed with mAuth: $mAuth")
                    onSuccess(ResponseValue(mAuth))
                }

                override fun onLoginFailed(errorCode: Int) {
                    Log.d(TAG, "onLoginFailed")
                    onError(ResponseError(errorCode))
                }
            })

//        } else {
//            return ErrorValue(ErrorCode.NO_INTERNET_CONNECTION)
//        }
    } catch (exception: Exception) {
        Log.d(TAG, "Inside .run failed with exceprtion=$exception")
        ResponseError(ErrorCode.UNKNOWN_ERROR)
    }


    class RequestValue(val activityContext: WeakReference<BaseActivity>) : CoroutineUsecase.RequestValue

    class ResponseValue(val mAuth: SocialAuth) : CoroutineUsecase.ResponseValue

    class ResponseError(val errorCode: Int) : CoroutineUsecase.ErrorValue

    companion object {
        const val TAG = "SigninGoogleUsecase"
    }
}