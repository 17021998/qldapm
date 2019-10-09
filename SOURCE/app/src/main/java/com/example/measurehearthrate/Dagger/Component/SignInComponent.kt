package com.example.measurehearthrate.Dagger.Component

import com.example.measurehearthrate.Dagger.Module.SignInModule
import com.example.measurehearthrate.Interface.ActivityScoped
import com.example.measurehearthrate.View.SignInFragment
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [SignInModule::class])
interface SignInComponent {
    fun inject(fragment: SignInFragment)
}