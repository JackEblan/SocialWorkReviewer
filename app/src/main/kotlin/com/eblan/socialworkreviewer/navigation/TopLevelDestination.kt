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

class CategoryDestination : HomeDestination {
    override val label: Int
        get() = R.string.category
    override val icon: ImageVector
        get() = Swr.Category
    override val contentDescription: Int
        get() = R.string.category
    override val route: KClass<*>
        get() = CategoryRouteData::class
}

class NewsDestination : HomeDestination {
    override val label: Int
        get() = R.string.news
    override val icon: ImageVector
        get() = Swr.Campaign
    override val contentDescription: Int
        get() = R.string.news
    override val route: KClass<*>
        get() = NewsRouteData::class
}

class SettingsDestination : HomeDestination {
    override val label: Int
        get() = R.string.settings
    override val icon: ImageVector
        get() = Swr.Settings
    override val contentDescription: Int
        get() = R.string.settings
    override val route: KClass<*>
        get() = SettingsRouteData::class
}

class AboutDestination : HomeDestination {
    override val label: Int
        get() = R.string.about
    override val icon: ImageVector
        get() = Swr.Info
    override val contentDescription: Int
        get() = R.string.about
    override val route: KClass<*>
        get() = AboutRouteData::class
}
