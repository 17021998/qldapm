package com.example.measurehearthrate.Dagger.Component

import com.example.measurehearthrate.Base.BaseActivity
import com.example.measurehearthrate.Base.BaseFragment
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Dagger.Module.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjection
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

    @NotNull
    fun plus(signUpModule: SignUpModule): SignUpComponent

    @NotNull
    fun plus(signInModule: SignInModule): SignInComponent

}
