package com.example.measurehearthrate.Dagger.Module

import com.example.measurehearthrate.Base.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule internal constructor(val mApplication: MyApplication) {

    @Provides
    fun provideApplication(): MyApplication {
        return mApplication
    }
}