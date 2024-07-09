package com.android.socialworkreviewer.framework.countdowntimer

import android.os.CountDownTimer
import com.android.socialworkreviewer.core.model.CountDownTime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject

internal class DefaultCountDownTimerWrapper @Inject constructor() : CountDownTimerWrapper {
    private var _countDownTimer: CountDownTimer? = null

    override fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long) = callbackFlow {
        _countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                trySendBlocking(
                    CountDownTime(
                        minutes = remainingTimeFormat(millisUntilFinished = millisUntilFinished),
                        isFinished = false
                    )
                ).onFailure {
                    _countDownTimer?.cancel()
                }
            }

            override fun onFinish() {
                trySendBlocking(
                    CountDownTime(
                        minutes = "", isFinished = true
                    )
                ).onFailure {
                    _countDownTimer?.cancel()
                }
            }
        }

        awaitClose {
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