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
package com.android.socialworkreviewer.core.testing.countdowntimer

import com.android.socialworkreviewer.core.model.CountDownTime
import com.android.socialworkreviewer.framework.countdowntimer.CountDownTimerWrapper
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeCountDownTimer : CountDownTimerWrapper {
    private var _countDownTimeFlow =
        MutableSharedFlow<CountDownTime>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override val countDownTimeFlow: SharedFlow<CountDownTime>
        get() = _countDownTimeFlow.asSharedFlow()

    override fun setCountDownTimer(millisInFuture: Long, countDownInterval: Long) {
        _countDownTimeFlow.tryEmit(
            CountDownTime(
                minutes = "",
                isFinished = false,
            ),
        )
    }

    override fun start() {
    }

    override fun cancel() {
    }
}
