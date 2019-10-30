package com.example.hearthrate.Base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel : ViewModel() {

    private val main = Dispatchers.Main
    protected val IO = Dispatchers.IO

    protected val uiScope = CoroutineScope(main)
    protected val bgScope = CoroutineScope((IO))

    abstract fun start()



}