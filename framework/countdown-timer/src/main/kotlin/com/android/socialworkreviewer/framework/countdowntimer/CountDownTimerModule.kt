package com.android.socialworkreviewer.framework.countdowntimer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface CountDownTimerModule {

    @Binds
    @Singleton
    fun countDownTimerWrapper(impl: DefaultCountDownTimerWrapper): CountDownTimerWrapper
}
