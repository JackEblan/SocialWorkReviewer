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
package com.eblan.socialworkreviewer.core.network.mapper

import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.network.model.CategoryDocument

internal fun toCategory(categoryDocument: CategoryDocument?): Category {
    val id = categoryDocument?.id.toString()

    val title = categoryDocument?.title.toString()

    val description = categoryDocument?.description.toString()

    val imageUrl = categoryDocument?.imageUrl.toString()

    val average = 0.0

    val questionSettings = categoryDocument?.questionSettings?.map { questionSettingDocument ->
        toQuestionSetting(questionSettingDocument = questionSettingDocument)
    } ?: emptyList()

    return Category(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        average = average,
        questionSettings = questionSettings,
    )
}
