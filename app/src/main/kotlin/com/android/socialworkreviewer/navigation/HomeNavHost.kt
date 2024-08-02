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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.android.socialworkreviewer.feature.announcement.navigation.announcementScreen
import com.android.socialworkreviewer.feature.category.navigation.CategoryRouteData
import com.android.socialworkreviewer.feature.category.navigation.categoryScreen
import com.android.socialworkreviewer.feature.settings.navigation.settingsScreen

@Composable
fun HomeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onCategoryClick: (String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CategoryRouteData::class,
    ) {
        categoryScreen(
            onCategoryClick = onCategoryClick,
        )

        announcementScreen()

        settingsScreen()
    }
}
