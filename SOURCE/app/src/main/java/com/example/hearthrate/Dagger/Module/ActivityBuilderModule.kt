package com.example.hearthrate.Dagger.Module

import com.example.hearthrate.View.SignUpFragment
import dagger.Module

@Module
abstract class ActivityBuilderModule {

    abstract fun contributeSignUpFragment() : SignUpFragment
}