package com.example.hearthrate.Dagger.Module

import android.content.Context
import com.example.hearthrate.Base.BaseActivity
import com.example.hearthrate.Base.MyApplication
import com.example.hearthrate.Usecase.SigninGoogleManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule internal constructor(val mApplication: MyApplication) {

    @Provides
    fun provideApplication(): MyApplication {
        return mApplication
    }

    @Provides
    fun provideBaseActivity(): BaseActivity {
        return BaseActivity()
    }

    @Singleton
    @Provides
    fun provideApplicationContext(): Context {
        return mApplication.applicationContext
    }

    @Singleton
    @Provides
    fun provideSigninGoogleManager(): SigninGoogleManager {
        return SigninGoogleManager()
    }



}