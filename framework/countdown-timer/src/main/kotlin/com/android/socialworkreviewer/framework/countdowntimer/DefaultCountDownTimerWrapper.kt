package com.android.socialworkreviewer.framework.countdowntimer

import android.os.CountDownTimer
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject

internal class DefaultCountDownTimerWrapper @Inject constructor() : CountDownTimerWrapper {
    private var _countDownTimer: CountDownTimer? = null

    private val _countDownTimerFinished = MutableSharedFlow<Boolean>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val countDownTimerFinished = _countDownTimerFinished.asSharedFlow()

    override fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long) = callbackFlow {
        _countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                trySendBlocking(remainingTimeFormat(millisUntilFinished = millisUntilFinished)).onFailure {
                    _countDownTimer?.cancel()
                }
            }

            override fun onFinish() {
                _countDownTimerFinished.tryEmit(true)
            }
        }

        awaitClose {
            _countDownTimerFinished.tryEmit(false)
            _countDownTimer?.cancel()
        }
    }

    override fun start() {
        _countDownTimer?.start()
    }

    override fun cancel() {
        _countDownTimer?.cancel()
    }

    private fun remainingTimeFormat(millisUntilFinished: Long): String {
        val totalSeconds = millisUntilFinished / 1000

        val minutes = totalSeconds / 60

        val remainingSeconds = totalSeconds % 60

        return String.format(Locale.getDefault(), "%02d %s %02d", minutes, ":", remainingSeconds)
    }
}