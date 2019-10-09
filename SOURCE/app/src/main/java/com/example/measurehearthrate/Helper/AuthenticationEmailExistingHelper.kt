package com.example.measurehearthrate.Helper

import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Helper.AuthenticationEmailExistingHelper.*
import com.example.measurehearthrate.Utils.CoroutineUsecase
import com.example.measurehearthrate.Utils.ResponseCode
import javax.inject.Inject

class AuthenticationEmailExistingHelper @Inject constructor() :
        CoroutineUsecase<RequestValues, ResponseValue, ResponseError>() {

    private var userDatabase = MyApplication.userDatabase

    override suspend fun run(requestValues: RequestValues?): Any? {
        if(requestValues == null) return ResponseError(ResponseCode.UNKNOWN_ERROR, "")

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