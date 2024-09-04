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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    val swrMultiThemes = listOf(
        SwrMultiTheme(
            greenTheme = true,
            purpleTheme = false,
            darkTheme = false,
            dynamicTheme = false,
            description = "GreenTheme",
        ),
        SwrMultiTheme(
            greenTheme = false,
            purpleTheme = true,
            darkTheme = false,
            dynamicTheme = false,
            description = "PurpleTheme",
        ),
        SwrMultiTheme(
            greenTheme = true,
            purpleTheme = false,
            darkTheme = true,
            dynamicTheme = false,
            description = "GreenDarkTheme",
        ),
        SwrMultiTheme(
            greenTheme = false,
            purpleTheme = true,
            darkTheme = true,
            dynamicTheme = false,
            description = "PurpleDarkTheme",
        ),
        SwrMultiTheme(
            greenTheme = false,
            purpleTheme = false,
            darkTheme = false,
            dynamicTheme = true,
            description = "DynamicTheme",
        ),
        SwrMultiTheme(
            greenTheme = false,
            purpleTheme = false,
            darkTheme = true,
            dynamicTheme = true,
            description = "DynamicDarkTheme",
        ),
    )

    var greenTheme by mutableStateOf(true)
    var purpleTheme by mutableStateOf(true)
    var darkTheme by mutableStateOf(true)
    var dynamicTheme by mutableStateOf(false)

    this.setContent {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
        ) {
            SwrTheme(
                greenTheme = greenTheme,
                purpleTheme = purpleTheme,
                darkTheme = darkTheme,
                dynamicTheme = dynamicTheme,
            ) {
                // Keying is necessary in some cases (e.g. animations)
                key(greenTheme, purpleTheme, darkTheme, dynamicTheme) {
                    content()
                }
            }
        }
    }

    swrMultiThemes.forEach { swrMultiTheme ->
        greenTheme = swrMultiTheme.greenTheme
        purpleTheme = swrMultiTheme.purpleTheme
        darkTheme = swrMultiTheme.darkTheme
        dynamicTheme = swrMultiTheme.dynamicTheme

        onCapture(
            "src/test/screenshots/" + "$name/${overrideFileName ?: name}" + swrMultiTheme.description + ".png",
            roborazziOptions,
        )
    }
}

private data class SwrMultiTheme(
    val greenTheme: Boolean,
    val purpleTheme: Boolean,
    val darkTheme: Boolean,
    val dynamicTheme: Boolean,
    val description: String,
)
