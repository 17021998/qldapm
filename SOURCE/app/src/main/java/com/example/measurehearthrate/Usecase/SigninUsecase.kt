package com.example.measurehearthrate.Usecase

import android.util.Log
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Database.UserDatabase
import com.example.measurehearthrate.Model.User
import com.example.measurehearthrate.R
import com.example.measurehearthrate.Utils.CoroutineUsecase
import com.example.measurehearthrate.Utils.MD5Algorithm
import com.example.measurehearthrate.Utils.ResponseCode
import javax.inject.Inject

class SigninUsecase @Inject constructor(private var userDatabase: UserDatabase):
        CoroutineUsecase<SigninUsecase.RequestValue,SigninUsecase.ResponseValue,SigninUsecase.ErrorValue>() {

    override suspend fun run(requestValues: RequestValue?): Any? {
        if(requestValues == null) {
            return ErrorValue(ResponseCode.UNKNOWN_ERROR,"")
        }
        val response = userDatabase.userDAO().getUserByEmail(requestValues.email)

        if(response != null) {
            Log.d(TAG,"Pass before encrypted: $response.mPassword")
            val encryptedPass = MD5Algorithm.md5Encrypt(requestValues.password)
            //val encryptedPass = Algorithm.md5(requestValues.password)
            Log.d(TAG,"Pass after encrypted: $encryptedPass")
            if(encryptedPass.equals(response.mPassword)) {
                return ResponseValue(response)
            } else {
                return ErrorValue(ResponseCode.WRONG_PASSWORD,MyApplication.instance.resources.getString(R.string.Login_Text__PasswordIncorrect))
            }
        } else {
            return ErrorValue(ResponseCode.WRONG_EMAIL,MyApplication.instance.resources.getString(R.string.Login_Text__EmailIsNotRegister))
        }
    }


    class RequestValue(val email: String, val password: String) : CoroutineUsecase.RequestValue

    class ResponseValue(val user: User?) : CoroutineUsecase.ResponseValue

    class ErrorValue(val errorCode: Int, val errorMessage: String) : CoroutineUsecase.ErrorValue

    companion object {
        const val TAG = "SigninUsecase"
    }

}