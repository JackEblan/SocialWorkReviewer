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
package com.android.socialworkreviewer.core.testing.repository

import com.android.socialworkreviewer.core.data.repository.CategoryRepository
import com.android.socialworkreviewer.core.model.Category
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeCategoryRepository : CategoryRepository {
    private val _categoriesFlow =
        MutableSharedFlow<List<Category>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val _currentCategories get() = _categoriesFlow.replayCache.firstOrNull() ?: emptyList()

    override fun getCategories(): Flow<List<Category>> {
        return _categoriesFlow
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return _currentCategories.find { category -> category.id == categoryId }
    }

    fun setCategories(value: List<Category>) {
        _categoriesFlow.tryEmit(value)
    }
}
