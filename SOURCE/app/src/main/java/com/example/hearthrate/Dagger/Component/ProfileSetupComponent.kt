package com.example.hearthrate.Dagger.Component

import com.example.hearthrate.Dagger.Module.ProfileSetupModule
import com.example.hearthrate.Interface.ActivityScoped
import com.example.hearthrate.View.ProfileSetupFragment
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [ProfileSetupModule::class])
interface ProfileSetupComponent {
    fun inject(profileSetupFragment: ProfileSetupFragment)
}