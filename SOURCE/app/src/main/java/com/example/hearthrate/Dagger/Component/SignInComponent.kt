package com.example.hearthrate.Dagger.Component

import com.example.hearthrate.Dagger.Module.SignInModule
import com.example.hearthrate.Interface.ActivityScoped
import com.example.hearthrate.View.SignInFragment
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [SignInModule::class])
interface SignInComponent {
    fun inject(fragment: SignInFragment)
}