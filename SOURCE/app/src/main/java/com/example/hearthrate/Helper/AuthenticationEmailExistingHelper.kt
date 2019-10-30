package com.example.hearthrate.Helper

import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.Helper.AuthenticationEmailExistingHelper.*
import com.example.hearthrate.Utils.CoroutineUsecase
import com.example.hearthrate.Utils.ErrorCode
import javax.inject.Inject

class AuthenticationEmailExistingHelper @Inject constructor() :
        CoroutineUsecase<RequestValues, ResponseValue, ResponseError>() {

    private var userDatabase = MyApplication.userDatabase

    override suspend fun run(requestValues: RequestValues?): Any? {
        if(requestValues == null) return ResponseError(ErrorCode.UNKNOWN_ERROR, "")

        val response = userDatabase.userDAO().getUserByEmail(requestValues.email)

        if(response != null) {
            return ResponseValue(true)
        } else {
            return ResponseValue(false)
        }
    }


    class RequestValues(val email: String) : CoroutineUsecase.RequestValue
    class ResponseError(val errorCode: Int, val errorMesagge: String) : CoroutineUsecase.ErrorValue
    class ResponseValue(val isExisting: Boolean) : CoroutineUsecase.ResponseValue


}