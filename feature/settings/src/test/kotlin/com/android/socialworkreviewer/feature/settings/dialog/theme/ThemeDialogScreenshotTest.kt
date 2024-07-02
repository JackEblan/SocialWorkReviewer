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
package com.android.socialworkreviewer.feature.settings.dialog.theme

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.android.socialworkreviewer.core.designsystem.component.SocialWorkReviewerBackground
import com.android.socialworkreviewer.core.designsystem.theme.SocialWorkReviewerTheme
import com.android.socialworkreviewer.core.screenshottesting.util.DefaultTestDevices
import com.android.socialworkreviewer.core.screenshottesting.util.captureDialogForDevice
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import kotlin.test.Test

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class ThemeDialogScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun themeDialog() {
        composeTestRule.captureDialogForDevice(
            fileName = "ThemeDialog",
            deviceName = "foldable",
            deviceSpec = DefaultTestDevices.FOLDABLE.spec,
        ) {
            SocialWorkReviewerTheme {
                ThemeDialog(
                    onDismissRequest = {},
                    selected = 0,
                    onSelect = {},
                    onCancelClick = {},
                    onChangeClick = {},
                    contentDescription = "",
                )
            }
        }
    }

    @Test
    fun themeDialog_dark() {
        composeTestRule.captureDialogForDevice(
            fileName = "ThemeDialog",
            deviceName = "foldable_dark",
            deviceSpec = DefaultTestDevices.FOLDABLE.spec,
            darkMode = true,
        ) {
            SocialWorkReviewerTheme {
                SocialWorkReviewerBackground {
                    ThemeDialog(
                        onDismissRequest = {},
                        selected = 0,
                        onSelect = {},
                        onCancelClick = {},
                        onChangeClick = {},
                        contentDescription = "",
                    )
                }
            }
        }
    }
}