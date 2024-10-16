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
package com.eblan.socialworkreviewer.feature.home

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eblan.socialworkreviewer.feature.home.navigation.HomeDestination
import kotlin.reflect.KClass

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
    topLevelDestinations: List<HomeDestination>,
    startDestination: KClass<*>,
    onItemClick: (NavHostController, HomeDestination) -> Unit,
    builder: NavGraphBuilder.() -> Unit,
) {
    val isOnline = viewModel.isOnline.collectAsStateWithLifecycle().value

    HomeScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        navController = navController,
        topLevelDestinations = topLevelDestinations,
        startDestination = startDestination,
        isOnline = isOnline,
        onItemClick = onItemClick,
        builder = builder,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
    topLevelDestinations: List<HomeDestination>,
    startDestination: KClass<*>,
    isOnline: Boolean?,
    onItemClick: (NavHostController, HomeDestination) -> Unit,
    builder: NavGraphBuilder.() -> Unit,
) {
    val topAppBarScrollBehavior = enterAlwaysScrollBehavior()

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val topBarTitleStringResource = topLevelDestinations.find { destination ->
        currentDestination.isTopLevelDestinationInHierarchy(destination.route)
    }?.label ?: topLevelDestinations.first().label

    LaunchedEffect(key1 = isOnline) {
        if (isOnline != null && isOnline.not()) {
            snackbarHostState.showSnackbar(
                message = "No internet connection",
                duration = SnackbarDuration.Indefinite,
            )
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

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
        navigationSuiteColors = NavigationSuiteDefaults.colors(navigationBarContainerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = topBarTitleStringResource),
                        )
                    },
                    modifier = modifier.testTag("home:largeTopAppBar"),
                    scrollBehavior = topAppBarScrollBehavior,
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            NavHost(
                modifier = modifier
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

private fun NavDestination?.isTopLevelDestinationInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
