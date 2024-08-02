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
package com.android.socialworkreviewer.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.android.socialworkreviewer.feature.announcement.navigation.navigateToAnnouncementScreen
import com.android.socialworkreviewer.feature.category.navigation.navigateToCategoryScreen
import com.android.socialworkreviewer.feature.home.navigation.HomeRouteData
import com.android.socialworkreviewer.feature.home.navigation.homeScreen
import com.android.socialworkreviewer.feature.question.navigation.navigateToQuestionScreen
import com.android.socialworkreviewer.feature.question.navigation.questionScreen
import com.android.socialworkreviewer.feature.settings.navigation.navigateToSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwrNavHost(modifier: Modifier = Modifier) {
    val swrNavHostController = rememberNavController()

    val homeNavHostController = rememberNavController()

    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    val homeNavigationItems = listOf(
        CategoryNavigationItem(),
        AnnouncementNavigationItem(),
        SettingsNavigationItem(),
    )

    NavHost(
        modifier = modifier,
        navController = swrNavHostController,
        startDestination = HomeRouteData::class,
    ) {
        homeScreen(
            navController = homeNavHostController,
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            items = homeNavigationItems,
            onItemClick = { homeNavigationItem ->
                when (homeNavigationItem) {
                    is CategoryNavigationItem -> homeNavHostController.navigateToCategoryScreen()
                    is AnnouncementNavigationItem -> homeNavHostController.navigateToAnnouncementScreen()
                    is SettingsNavigationItem -> homeNavHostController.navigateToSettings()
                }
            },
            content = { paddingValues ->
                HomeNavHost(
                    modifier = Modifier
                        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues),
                    navController = homeNavHostController,
                    onCategoryClick = swrNavHostController::navigateToQuestionScreen,
                )
            },
        )

        questionScreen(onNavigateUp = swrNavHostController::navigateUp)
    }
}
