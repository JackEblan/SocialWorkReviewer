/*
 *
 *   Copyright 2023 Einstein Blanco
 *
 *   Licensed under the GNU General Public License v3.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.eblan.socialworkreviewer.framework.countdowntimer

import android.os.CountDownTimer
import com.eblan.socialworkreviewer.core.model.CountDownTime
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

internal class AndroidCountDownTimerWrapper @Inject constructor() : CountDownTimerWrapper {
    private var _countDownTimer: CountDownTimer? = null

    private var _countDownTimeFlow =
        MutableSharedFlow<CountDownTime>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val countDownTimeFlow = _countDownTimeFlow.asSharedFlow()

    override fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long) {
        _countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _countDownTimeFlow.tryEmit(
                    CountDownTime(
                        minutes = remainingTimeFormat(millisUntilFinished = millisUntilFinished),
                        isFinished = false,
                    ),
                )
            }

            override fun onFinish() {
                _countDownTimeFlow.tryEmit(
                    CountDownTime(
                        minutes = "",
                        isFinished = true,
                    ),
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

        _countDownTimeFlow.resetReplayCache()
    }

    private fun remainingTimeFormat(millisUntilFinished: Long): String {
        val totalSeconds = millisUntilFinished / 1000

        val minutes = totalSeconds / 60

        val seconds = totalSeconds % 60

        return "%02d:%02d".format(minutes, seconds)
    }
}
