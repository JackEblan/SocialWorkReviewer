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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.eblan.socialworkreviewer.core.designsystem.component.SwrLargeTopAppBar
import com.eblan.socialworkreviewer.core.designsystem.theme.LocalGradientColors
import com.eblan.socialworkreviewer.core.designsystem.theme.SwrTheme
import com.eblan.socialworkreviewer.core.screenshottesting.util.DefaultRoborazziOptions
import com.eblan.socialworkreviewer.core.screenshottesting.util.captureMultiTheme
import com.github.takahirom.roborazzi.captureRoboImage
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class TopAppBarScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun largeTopAppBar_multipleThemes() {
        composeTestRule.captureMultiTheme("LargeTopAppBar") {
            SwrLargeTopAppBarExample()
        }
    }

    @Test
    fun largeTopAppBar_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    DeviceConfigurationOverride.FontScale(2f),
                ) {
                    SwrTheme {
                        SwrLargeTopAppBarExample()
                    }
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage(
            "src/test/screenshots/LargeTopAppBar/LargeTopAppBar_fontScale2.png",
            roborazziOptions = DefaultRoborazziOptions,
        )
    }

    @Composable
    private fun SwrLargeTopAppBarExample() {
        SwrLargeTopAppBar(
            title = {
                Text(
                    text = "Large Top App Bar",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        brush = Brush.linearGradient(
                            colors = LocalGradientColors.current.topBarTitleColorsDefault,
                        ),
                    ),
                )
            },
        )
    }
}
