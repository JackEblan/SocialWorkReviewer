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
package com.eblan.socialworkreviewer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.eblan.socialworkreviewer.feature.announcement.navigation.announcementScreen
import com.eblan.socialworkreviewer.feature.announcement.navigation.navigateToAnnouncementScreen
import com.eblan.socialworkreviewer.feature.category.navigation.CategoryRouteData
import com.eblan.socialworkreviewer.feature.category.navigation.categoryScreen
import com.eblan.socialworkreviewer.feature.category.navigation.navigateToCategoryScreen
import com.eblan.socialworkreviewer.feature.home.navigation.HomeRouteData
import com.eblan.socialworkreviewer.feature.home.navigation.homeScreen
import com.eblan.socialworkreviewer.feature.question.navigation.navigateToQuestionScreen
import com.eblan.socialworkreviewer.feature.question.navigation.questionScreen
import com.eblan.socialworkreviewer.feature.settings.navigation.navigateToSettings
import com.eblan.socialworkreviewer.feature.settings.navigation.settingsScreen

@Composable
fun SwrNavHost(modifier: Modifier = Modifier) {
    val swrNavHostController = rememberNavController()

    val topLevelDestinations = listOf(
        CategoryDestination(),
        AnnouncementDestination(),
        SettingsDestination(),
    )

    NavHost(
        modifier = modifier,
        navController = swrNavHostController,
        startDestination = HomeRouteData::class,
    ) {
        homeScreen(
            topLevelDestinations = topLevelDestinations,
            startDestination = CategoryRouteData::class,
            onItemClick = { homeNavHostController, homeDestination ->
                when (homeDestination) {
                    is CategoryDestination -> homeNavHostController.navigateToCategoryScreen()
                    is AnnouncementDestination -> homeNavHostController.navigateToAnnouncementScreen()
                    is SettingsDestination -> homeNavHostController.navigateToSettings()
                }
            },
            builder = {
                categoryScreen(onCategoryClick = swrNavHostController::navigateToQuestionScreen)

                announcementScreen()

                settingsScreen()
            },
        )

        questionScreen(onNavigateUp = swrNavHostController::navigateUp)
    }
}
