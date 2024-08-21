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
package com.android.socialworkreviewer.feature.home

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.socialworkreviewer.feature.home.navigation.HomeDestination
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRoute(
    navController: NavHostController = rememberNavController(),
    topLevelDestinations: List<HomeDestination>,
    startDestination: KClass<*>,
    onItemClick: (NavHostController, HomeDestination) -> Unit,
    builder: NavGraphBuilder.() -> Unit,
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val topBarTitleStringResource = topLevelDestinations.find { destination ->
        currentDestination.isTopLevelDestinationInHierarchy(destination.route)
    }?.label ?: topLevelDestinations.first().label

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            topLevelDestinations.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(id = destination.contentDescription),
                        )
                    },
                    label = { Text(stringResource(id = destination.label)) },
                    selected = currentDestination.isTopLevelDestinationInHierarchy(destination.route),
                    onClick = {
                        onItemClick(navController, destination)
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                HomeLargeTopAppBar(
                    title = stringResource(id = topBarTitleStringResource),
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            },
        ) { paddingValues ->
            NavHost(
                modifier = Modifier
                    .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
                navController = navController,
                startDestination = startDestination,
                builder = builder,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeLargeTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    val gradientColors = listOf(Color(0xFF00BCD4), Color(0xFF03A9F4), Color(0xFF9C27B0))

    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                    ),
                ),
            )
        },
        modifier = modifier.testTag("largeTopAppBar"),
        scrollBehavior = topAppBarScrollBehavior,
    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
