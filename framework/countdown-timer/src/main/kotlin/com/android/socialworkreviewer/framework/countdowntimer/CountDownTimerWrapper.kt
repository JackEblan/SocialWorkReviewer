package com.android.socialworkreviewer.framework.countdowntimer

import com.android.socialworkreviewer.core.model.CountDownTime
import kotlinx.coroutines.flow.Flow

interface CountDownTimerWrapper {
    fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long): Flow<CountDownTime>

    fun start()

    fun cancel()
}