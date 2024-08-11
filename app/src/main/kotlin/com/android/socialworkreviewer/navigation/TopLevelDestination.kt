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

import androidx.compose.ui.graphics.vector.ImageVector
import com.android.socialworkreviewer.R
import com.android.socialworkreviewer.core.designsystem.icon.Swr
import com.android.socialworkreviewer.feature.announcement.navigation.AnnouncementRouteData
import com.android.socialworkreviewer.feature.category.navigation.CategoryRouteData
import com.android.socialworkreviewer.feature.home.navigation.HomeDestination
import com.android.socialworkreviewer.feature.settings.navigation.SettingsRouteData
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

class AnnouncementDestination : HomeDestination {
    override val label: Int
        get() = R.string.announcement
    override val icon: ImageVector
        get() = Swr.Campaign
    override val contentDescription: Int
        get() = R.string.announcement
    override val route: KClass<*>
        get() = AnnouncementRouteData::class
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
