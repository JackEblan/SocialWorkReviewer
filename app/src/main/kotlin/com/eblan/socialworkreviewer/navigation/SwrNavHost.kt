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

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.eblan.socialworkreviewer.feature.about.navigation.aboutScreen
import com.eblan.socialworkreviewer.feature.about.navigation.navigateToAboutScreen
import com.eblan.socialworkreviewer.feature.category.navigation.CategoryRouteData
import com.eblan.socialworkreviewer.feature.category.navigation.categoryScreen
import com.eblan.socialworkreviewer.feature.category.navigation.navigateToCategoryScreen
import com.eblan.socialworkreviewer.feature.home.navigation.HomeRouteData
import com.eblan.socialworkreviewer.feature.home.navigation.homeScreen
import com.eblan.socialworkreviewer.feature.news.navigation.navigateToNewsScreen
import com.eblan.socialworkreviewer.feature.news.navigation.newsScreen
import com.eblan.socialworkreviewer.feature.question.navigation.navigateToQuestionScreen
import com.eblan.socialworkreviewer.feature.question.navigation.questionScreen
import com.eblan.socialworkreviewer.feature.settings.navigation.navigateToSettings
import com.eblan.socialworkreviewer.feature.settings.navigation.settingsScreen
import com.eblan.socialworkreviewer.navigation.TopLevelDestination.ABOUT
import com.eblan.socialworkreviewer.navigation.TopLevelDestination.CATEGORY
import com.eblan.socialworkreviewer.navigation.TopLevelDestination.NEWS
import com.eblan.socialworkreviewer.navigation.TopLevelDestination.SETTINGS

@Composable
fun SwrNavHost(modifier: Modifier = Modifier) {
    val swrNavHostController = rememberNavController()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    NavHost(
        modifier = modifier,
        navController = swrNavHostController,
        startDestination = HomeRouteData::class,
    ) {
        homeScreen(
            snackbarHostState = snackbarHostState,
            topLevelDestinations = TopLevelDestination.entries,
            startDestination = CategoryRouteData::class,
            onItemClick = { homeNavHostController, homeDestination ->
                when (homeDestination) {
                    CATEGORY -> homeNavHostController.navigateToCategoryScreen()
                    NEWS -> homeNavHostController.navigateToNewsScreen()
                    SETTINGS -> homeNavHostController.navigateToSettings()
                    ABOUT -> homeNavHostController.navigateToAboutScreen()
                }
            },
            builder = {
                categoryScreen(onCategoryClick = swrNavHostController::navigateToQuestionScreen)

                newsScreen()

                settingsScreen()

                aboutScreen(snackbarHostState = snackbarHostState)
            },
        )

        questionScreen(onNavigateUp = swrNavHostController::navigateUp)
    }
}
