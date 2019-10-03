package com.example.measurehearthrate.Dagger.Module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.measurehearthrate.Factory.AppViewModelFactory
import com.example.measurehearthrate.Interface.ViewModelKey
import com.example.measurehearthrate.ViewModel.SignUpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindWatchSettingViewModel(modelHome: SignUpViewModel): ViewModel

}