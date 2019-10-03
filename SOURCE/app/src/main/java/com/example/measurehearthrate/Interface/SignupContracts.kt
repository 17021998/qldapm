package com.example.measurehearthrate.Interface

interface SignupContracts {

    fun enableSignup()
    fun disableSignup()
    fun navigateHome()
    fun onPasswordValidation(isMinCharacterMet: Boolean, isCombineLetterDigitMet: Boolean)
    fun onEmailValidation(isMet: Boolean, enableError: Boolean, errorMessage: String)


}