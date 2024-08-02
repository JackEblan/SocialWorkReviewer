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
package com.android.socialworkreviewer.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.android.socialworkreviewer.feature.home.HomeRoute

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreen(
    navController: NavHostController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    items: List<HomeNavigationItem>,
    onItemClick: (HomeNavigationItem) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    composable<HomeRouteData> {
        HomeRoute(
            navController = navController,
            topAppBarScrollBehavior = topAppBarScrollBehavior,
            items = items,
            onItemClick = onItemClick,
            content = content,
        )
    }
}
