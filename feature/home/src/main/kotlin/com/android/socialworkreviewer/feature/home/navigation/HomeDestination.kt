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

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.android.socialworkreviewer.core.designsystem.icon.Swr
import com.android.socialworkreviewer.feature.home.R

enum class HomeDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int,
) {
    CATEGORY(
        R.string.category,
        Swr.Category,
        R.string.category,
    ),
    ANNOUNCEMENT(
        R.string.announcement,
        Swr.Campaign,
        R.string.announcement,
    ),
    SETTINGS(R.string.settings, Swr.Settings, R.string.category),
}
