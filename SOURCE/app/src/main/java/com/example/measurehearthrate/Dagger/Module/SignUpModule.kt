package com.example.measurehearthrate.Dagger.Module

import androidx.annotation.NonNull
import com.example.measurehearthrate.Base.BaseFragment
import dagger.Module
import dagger.Provides

@Module
class SignUpModule (val mContext: BaseFragment) {
    @Provides
    @NonNull
    fun provideContext() : BaseFragment {
        return mContext
    }

}