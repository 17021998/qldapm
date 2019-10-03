package com.example.measurehearthrate.Dagger.Component

import com.example.measurehearthrate.Dagger.Module.SignUpModule
import com.example.measurehearthrate.View.SignUpActivity
import com.example.measurehearthrate.View.SignUpFragment
import dagger.Component
import dagger.Subcomponent


@Subcomponent(modules = [(SignUpModule::class)])
public interface SignUpComponent {
    fun inject(view: SignUpFragment)
}