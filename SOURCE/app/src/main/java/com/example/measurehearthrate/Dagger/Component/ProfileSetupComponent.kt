package com.example.measurehearthrate.Dagger.Component

import com.example.measurehearthrate.Dagger.Module.ProfileSetupModule
import com.example.measurehearthrate.Interface.ActivityScoped
import com.example.measurehearthrate.View.ProfileSetupFragment
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [ProfileSetupModule::class])
interface ProfileSetupComponent {
    fun inject(profileSetupFragment: ProfileSetupFragment)
}