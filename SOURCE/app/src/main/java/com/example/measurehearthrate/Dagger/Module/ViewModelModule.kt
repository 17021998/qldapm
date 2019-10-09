package com.example.measurehearthrate.Dagger.Module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.measurehearthrate.Factory.AppViewModelFactory
import com.example.measurehearthrate.Interface.ViewModelKey
import com.example.measurehearthrate.ViewModel.ProfileSetupViewModel
import com.example.measurehearthrate.ViewModel.SignUpViewModel
import com.example.measurehearthrate.ViewModel.SigninViewModel
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
    abstract fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SigninViewModel::class)
    abstract fun bindSignInViewModel(viewModel: SigninViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileSetupViewModel::class)
    abstract fun bindProfileSetupViewModel(viewModel: ProfileSetupViewModel): ViewModel
}