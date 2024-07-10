package com.android.socialworkreviewer.framework.countdowntimer

import com.android.socialworkreviewer.core.model.CountDownTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface CountDownTimerWrapper {
    val countDownTimeFlow: SharedFlow<CountDownTime>

    fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long)

    fun start()

    fun cancel()
}