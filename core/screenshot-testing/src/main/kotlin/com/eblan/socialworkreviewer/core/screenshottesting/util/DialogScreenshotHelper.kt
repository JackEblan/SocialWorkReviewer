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
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage

/**
 * Experimental feature to capture the entire screen, including dialogs combining light/dark and default/Android themes and whether dynamic color
 * is enabled.
 */
@OptIn(ExperimentalRoborazziApi::class)
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureDialogForDevice(
    fileName: String,
    deviceName: String,
    deviceSpec: String,
    darkMode: Boolean = false,
    body: @Composable () -> Unit,
) {
    captureScreenForDevice(
        fileName = fileName,
        deviceName = deviceName,
        deviceSpec = deviceSpec,
        darkMode = darkMode,
        body = body,
        onCapture = { filePath, roborazziOptions ->
            captureScreenRoboImage(
                filePath = filePath,
                roborazziOptions = roborazziOptions,
            )
        },
    )
}
