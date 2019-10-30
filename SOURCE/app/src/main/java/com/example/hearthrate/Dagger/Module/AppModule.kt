package com.example.hearthrate.Dagger.Module

import com.example.hearthrate.Base.MyApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule internal constructor(val mApplication: MyApplication) {

    @Provides
    fun provideApplication(): MyApplication {
        return mApplication
    }



}