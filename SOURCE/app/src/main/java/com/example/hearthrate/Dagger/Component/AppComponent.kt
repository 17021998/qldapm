package com.example.hearthrate.Dagger.Component

import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Base.BaseFragment
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.Dagger.Module.*
import com.example.hearthrate.Usecase.SigninGoogleManager
import com.example.hearthrate.Usecase.SigninGoogleUsecase
import com.example.hearthrate.Usecase.SigninUsecase
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import org.jetbrains.annotations.NotNull
import javax.inject.Singleton

@SuppressWarnings("unused", "NullableProblems")
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class,
    AppModule::class,
    AppDatabaseModule::class,
    ViewModelModule::class])

interface AppComponent  {


    fun inject(application: MyApplication)

    fun inject(activity: BaseActivity)

    fun inject(fragment: BaseFragment)

    fun inject(signinGoogleUsecase: SigninGoogleUsecase)

    fun inject(signinGoogleManager: SigninGoogleManager)

    fun inject(signinUsecase: SigninUsecase)

    @NotNull
    fun plus(signUpModule: SignUpModule): SignUpComponent

    @NotNull
    fun plus(signInModule: SignInModule): SignInComponent

    @NotNull
    fun plus(profileSetupModule: ProfileSetupModule): ProfileSetupComponent

}
