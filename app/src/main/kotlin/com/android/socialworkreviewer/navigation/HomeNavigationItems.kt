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
import com.android.socialworkreviewer.core.designsystem.icon.Swr
import com.android.socialworkreviewer.feature.announcement.navigation.AnnouncementRouteData
import com.android.socialworkreviewer.feature.category.navigation.CategoryRouteData
import com.android.socialworkreviewer.feature.home.navigation.HomeNavigationItem
import com.android.socialworkreviewer.feature.settings.navigation.SettingsRouteData
import kotlin.reflect.KClass

data class CategoryNavigationItem(
    override val title: String = "Category",
    override val contentDescription: String = "Category",
    override val icon: ImageVector = Swr.Category,
    override val route: KClass<*> = CategoryRouteData::class,
) : HomeNavigationItem

data class AnnouncementNavigationItem(
    override val title: String = "Announcement",
    override val contentDescription: String = "Announcement",
    override val icon: ImageVector = Swr.Campaign,
    override val route: KClass<*> = AnnouncementRouteData::class,
) : HomeNavigationItem

data class SettingsNavigationItem(
    override val title: String = "Settings",
    override val contentDescription: String = "Settings",
    override val icon: ImageVector = Swr.Settings,
    override val route: KClass<*> = SettingsRouteData::class,
) : HomeNavigationItem
