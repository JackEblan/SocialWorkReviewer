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
package com.eblan.socialworkreviewer.core.screenshottesting.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.eblan.socialworkreviewer.core.designsystem.theme.SwrTheme
import com.github.takahirom.roborazzi.RoborazziOptions
import com.google.accompanist.testharness.TestHarness
import org.robolectric.RuntimeEnvironment

internal fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureScreenForDevice(
    fileName: String,
    deviceName: String,
    deviceSpec: String,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    darkMode: Boolean = false,
    body: @Composable () -> Unit,
    onCapture: (filePath: String, roborazziOptions: RoborazziOptions) -> Unit,
) {
    val (width, height, dpi) = extractSpecs(deviceSpec)

    // Set qualifiers from specs
    RuntimeEnvironment.setQualifiers("w${width}dp-h${height}dp-${dpi}dpi")

    this.activity.setContent {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
        ) {
            TestHarness(darkMode = darkMode) {
                body()
            }
        }
    }

    onCapture("src/test/screenshots/${fileName}_$deviceName.png", roborazziOptions)
}

internal fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureMultiTheme(
    name: String,
    overrideFileName: String? = null,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    content: @Composable () -> Unit,
    onCapture: (
        filePath: String,
        roborazziOptions: RoborazziOptions,
    ) -> Unit,
) {
    this.setContent {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
        ) {
            listOf(
                Triple(true, false, "GreenTheme"),
                Triple(false, true, "PurpleTheme"),
                Triple(true, false, "GreenDarkTheme"),
                Triple(false, true, "PurpleDarkTheme"),
                Triple(false, false, "DynamicTheme"),
                Triple(false, false, "DynamicDarkTheme"),
            ).forEach { (isGreen, isPurple, description) ->
                val isDark = description.contains("Dark")
                val isDynamic = description.contains("Dynamic")

                SwrTheme(
                    greenTheme = isGreen,
                    purpleTheme = isPurple,
                    darkTheme = isDark,
                    dynamicTheme = isDynamic,
                ) {
                    key(isGreen, isPurple, isDark, isDynamic) {
                        content()
                    }
                }

                val filePath =
                    "src/test/screenshots/$name/${overrideFileName ?: name}$description.png"
                onCapture(filePath, roborazziOptions)
            }
        }
    }
}
