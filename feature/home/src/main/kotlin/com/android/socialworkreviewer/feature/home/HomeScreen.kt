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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.android.socialworkreviewer.feature.home.navigation.HomeDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRoute(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onItemClick: (HomeDestination) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    var currentDestination by rememberSaveable { mutableStateOf(HomeDestination.CATEGORY) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            HomeDestination.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(id = destination.contentDescription),
                        )
                    },
                    label = { Text(stringResource(id = destination.label)) },
                    selected = destination == currentDestination,
                    onClick = {
                        currentDestination = destination

                        onItemClick(destination)
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                HomeLargeTopAppBar(
                    title = stringResource(id = currentDestination.label),
                    topAppBarScrollBehavior = topAppBarScrollBehavior,
                )
            },
        ) { paddingValues ->
            content(paddingValues)
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
