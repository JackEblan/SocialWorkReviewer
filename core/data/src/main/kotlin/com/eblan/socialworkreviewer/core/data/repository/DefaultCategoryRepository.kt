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
package com.eblan.socialworkreviewer.core.data.repository

import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.network.firestore.CategoryDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultCategoryRepository @Inject constructor(private val categoryDataSource: CategoryDataSource) :
    CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return categoryDataSource.getCategoryDocuments()
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return categoryDataSource.getCategoryDocument(categoryDocumentId = categoryId)
    }
}
