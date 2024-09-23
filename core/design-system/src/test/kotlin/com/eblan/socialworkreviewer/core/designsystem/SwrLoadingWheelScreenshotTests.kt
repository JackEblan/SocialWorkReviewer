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
package com.eblan.socialworkreviewer.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLoadingWheel
import com.eblan.socialworkreviewer.core.designsystem.component.SwrOverlayLoadingWheel
import com.eblan.socialworkreviewer.core.designsystem.theme.SwrTheme
import com.eblan.socialworkreviewer.core.screenshottesting.util.DefaultRoborazziOptions
import com.eblan.socialworkreviewer.core.screenshottesting.util.captureScreenMultiTheme
import com.github.takahirom.roborazzi.captureRoboImage
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class SwrLoadingWheelScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun swrLoadingWheel_multipleThemes() {
        composeTestRule.captureScreenMultiTheme("LoadingWheel") {
            Surface {
                SwrLoadingWheel(contentDescription = "test")
            }
        }
    }

    @Test
    fun swrOverlayLoadingWheel_multipleThemes() {
        composeTestRule.captureScreenMultiTheme("LoadingWheel", "OverlayLoadingWheel") {
            Surface {
                SwrOverlayLoadingWheel(contentDescription = "test")
            }
        }
    }

    @Test
    fun swrLoadingWheel_animation() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            SwrTheme {
                SwrLoadingWheel(contentDescription = "")
            }
        }
        // Try multiple frames of the animation; some arbitrary, some synchronized with duration.
        listOf(20L, 115L, 724L, 1000L).forEach { deltaTime ->
            composeTestRule.mainClock.advanceTimeBy(deltaTime)
            composeTestRule.onRoot().captureRoboImage(
                "src/test/screenshots/LoadingWheel/LoadingWheel_animation_$deltaTime.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
        }
    }
}
