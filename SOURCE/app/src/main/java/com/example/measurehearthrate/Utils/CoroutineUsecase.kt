package com.example.measurehearthrate.Utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class CoroutineUsecase<in Q: CoroutineUsecase.RequestValue, R: CoroutineUsecase.ResponseValue, E:CoroutineUsecase.ErrorValue> {


    private var mCallBack: UseCaseCallBack<R,E> ?= null
    abstract suspend fun run(requestValues: Q?): Any?
    protected val mUseCaseScope = CoroutineScope(Dispatchers.Default)


    fun executeUsecase (requestValues: Q?, callBack: UseCaseCallBack<R, E>?) = mUseCaseScope.launch {
        mCallBack = callBack

        when (val response = run(requestValues)) {
            is ResponseValue -> onSuccess(response as R)
            is ErrorValue -> onError(response as E)
        }
    }


    interface ResponseValue

    interface RequestValue

    interface ErrorValue

    interface UseCaseCallBack<in R, in E> {

        fun onSuccess(responseValue: R)

        fun onError(errorValue: E)
    }

    fun onSuccess(response: R) = mUseCaseScope.launch(Dispatchers.Main) {
        mCallBack?.onSuccess(response)
    }

    fun onError(errorValue: E) = mUseCaseScope.launch(Dispatchers.Main) {
        mCallBack?.onError(errorValue)
    }
}