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

import androidx.compose.ui.graphics.vector.ImageVector
import com.eblan.socialworkreviewer.R
import com.eblan.socialworkreviewer.core.designsystem.icon.Swr
import com.eblan.socialworkreviewer.feature.about.navigation.AboutRouteData
import com.eblan.socialworkreviewer.feature.category.navigation.CategoryRouteData
import com.eblan.socialworkreviewer.feature.home.navigation.HomeDestination
import com.eblan.socialworkreviewer.feature.news.navigation.NewsRouteData
import com.eblan.socialworkreviewer.feature.settings.navigation.SettingsRouteData
import kotlin.reflect.KClass

enum class TopLevelDestination(
    override val label: Int,
    override val icon: ImageVector,
    override val contentDescription: Int,
    override val route: KClass<*>,
) : HomeDestination {
    CATEGORY(
        label = R.string.category,
        icon = Swr.Category,
        contentDescription = R.string.category,
        route = CategoryRouteData::class,
    ),

    NEWS(
        label = R.string.news,
        icon = Swr.Campaign,
        contentDescription = R.string.news,
        route = NewsRouteData::class,
    ),

    SETTINGS(
        label = R.string.settings,
        icon = Swr.Settings,
        contentDescription = R.string.settings,
        route = SettingsRouteData::class,
    ),

    ABOUT(
        label = R.string.about,
        icon = Swr.Info,
        contentDescription = R.string.about,
        route = AboutRouteData::class,
    )
}
