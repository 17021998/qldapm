package com.example.measurehearthrate.Dagger.Module

import com.example.measurehearthrate.View.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    abstract fun contributeSignUpFragment() : SignUpFragment
}