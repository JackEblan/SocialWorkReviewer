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
package com.eblan.socialworkreviewer.feature.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.eblan.socialworkreviewer.core.model.DarkThemeConfig
import com.eblan.socialworkreviewer.core.model.ThemeBrand
import com.eblan.socialworkreviewer.core.model.UserData
import org.junit.Rule
import org.junit.Test

class SettingsScreenDialogsTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun themeDialog_isDisplayed_whenThemeSetting_isClicked_thenDismissed() {
        composeTestRule.setContent {
            SettingsScreen(
                settingsUiState = SettingsUiState.Success(
                    UserData(
                        themeBrand = ThemeBrand.DEFAULT,
                        useDynamicColor = true,
                        darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    ),
                ),
                supportDynamicColor = true,
                onUpdateThemeBrand = {},
                onUpdateDarkThemeConfig = {},
                onChangeDynamicColorPreference = {},
            )
        }

        composeTestRule.onNodeWithTag("settings:theme").performClick()

        composeTestRule.onNodeWithContentDescription("Theme Dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Theme Dialog").assertIsNotDisplayed()
    }

    @Test
    fun darkDialog_isDisplayed_whenDarkSetting_isClicked_thenDismissed() {
        composeTestRule.setContent {
            SettingsScreen(
                settingsUiState = SettingsUiState.Success(
                    UserData(
                        themeBrand = ThemeBrand.DEFAULT,
                        useDynamicColor = true,
                        darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    ),
                ),
                supportDynamicColor = true,
                onUpdateThemeBrand = {},
                onUpdateDarkThemeConfig = {},
                onChangeDynamicColorPreference = {},
            )
        }

        composeTestRule.onNodeWithTag("settings:dark").performClick()

        composeTestRule.onNodeWithContentDescription("Dark Dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Dark Dialog").assertIsNotDisplayed()
    }

    @Test
    fun themeDialog_stateRestoration() {
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            SettingsScreen(
                settingsUiState = SettingsUiState.Success(
                    UserData(
                        themeBrand = ThemeBrand.DEFAULT,
                        useDynamicColor = true,
                        darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    ),
                ),
                supportDynamicColor = true,
                onUpdateThemeBrand = {},
                onUpdateDarkThemeConfig = {},
                onChangeDynamicColorPreference = {},
            )
        }

        composeTestRule.onNodeWithTag("settings:theme").performClick()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithContentDescription("Theme Dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Theme Dialog").assertIsNotDisplayed()
    }

    @Test
    fun darkDialog_stateRestoration() {
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            SettingsScreen(
                settingsUiState = SettingsUiState.Success(
                    UserData(
                        themeBrand = ThemeBrand.DEFAULT,
                        useDynamicColor = true,
                        darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    ),
                ),
                supportDynamicColor = true,
                onUpdateThemeBrand = {},
                onUpdateDarkThemeConfig = {},
                onChangeDynamicColorPreference = {},
            )
        }

        composeTestRule.onNodeWithTag("settings:dark").performClick()

        restorationTester.emulateSavedInstanceStateRestore()

        composeTestRule.onNodeWithContentDescription("Dark Dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Dark Dialog").assertIsNotDisplayed()
    }
}
