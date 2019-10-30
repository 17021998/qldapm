package com.example.hearthrate.Dagger.Component

import com.example.hearthrate.Dagger.Module.SignUpModule
import com.example.hearthrate.Interface.ActivityScoped
import com.example.hearthrate.View.SignUpFragment
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [(SignUpModule::class)])
interface SignUpComponent {
    fun inject(view: SignUpFragment)
}