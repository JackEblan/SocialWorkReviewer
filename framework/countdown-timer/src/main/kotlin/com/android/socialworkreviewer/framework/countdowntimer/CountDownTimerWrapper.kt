package com.android.socialworkreviewer.framework.countdowntimer

import kotlinx.coroutines.flow.Flow

interface CountDownTimerWrapper {
    val countDownTimerFinished: Flow<Boolean>

    fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long): Flow<String>

    fun start()

    fun cancel()
}