package com.android.socialworkreviewer.framework.countdowntimer

import android.os.CountDownTimer
import com.android.socialworkreviewer.core.model.CountDownTime
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.Locale
import javax.inject.Inject

internal class DefaultCountDownTimerWrapper @Inject constructor() : CountDownTimerWrapper {
    private var _countDownTimer: CountDownTimer? = null

    private var _countDownTimeFlow =
        MutableSharedFlow<CountDownTime>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val countDownTimeFlow = _countDownTimeFlow.asSharedFlow()

    override fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long) {
        _countDownTimer?.cancel()

        _countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _countDownTimeFlow.tryEmit(
                    CountDownTime(
                        minutes = remainingTimeFormat(millisUntilFinished = millisUntilFinished),
                        isFinished = false
                    )
                )
            }

            override fun onFinish() {
                _countDownTimeFlow.tryEmit(
                    CountDownTime(
                        minutes = "", isFinished = true
                    )
                )

                _countDownTimeFlow.resetReplayCache()
            }
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