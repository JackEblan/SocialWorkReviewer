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
package com.eblan.socialworkreviewer.benchmarks.announcement

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.eblan.socialworkreviewer.benchmarks.PACKAGE_NAME
import com.eblan.socialworkreviewer.benchmarks.waitForLoadingWheelToDisappear
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ScrollAnnouncementBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollAnnouncementCompilationNone() = scrollAnnouncement(CompilationMode.None())

    @Test
    fun scrollAnnouncementCompilationBaselineProfile() = scrollAnnouncement(CompilationMode.Partial())

    @Test
    fun scrollAnnouncementCompilationFull() = scrollAnnouncement(CompilationMode.Full())

    private fun scrollAnnouncement(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = compilationMode,
        iterations = 10,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
        },
    ) {
        navigateToAnnouncementScreen()
        waitForLoadingWheelToDisappear()
        announcementScrollDownUp()
    }
}
