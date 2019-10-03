package com.example.measurehearthrate.Dagger.Component

import android.content.Context
import com.example.measurehearthrate.Base.MyApplication
import com.example.measurehearthrate.Dagger.Module.AppModule
import com.example.measurehearthrate.Dagger.Module.SignUpModule
import com.example.measurehearthrate.Dagger.Module.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun inject(application: MyApplication)

    //fun plus(signUpModule: SignUpModule): SignUpComponent

    //fun context(): Context
}
