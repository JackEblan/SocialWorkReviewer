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
package com.eblan.socialworkreviewer.feature.category

import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.testing.repository.FakeCategoryRepository
import com.eblan.socialworkreviewer.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertIs

class CategoryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var categoryRepository: FakeCategoryRepository

    private lateinit var viewModel: CategoryViewModel

    @Before
    fun setup() {
        categoryRepository = FakeCategoryRepository()

        viewModel = CategoryViewModel(
            categoryRepository = categoryRepository,
        )
    }

    @Test
    fun categoryUiState_isLoading_whenStarted() {
        assertIs<CategoryUiState.Loading>(viewModel.categoryUiState.value)
    }

    @Test
    fun categoryUiState_isSuccess() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.categoryUiState.collect()
        }

        val categories = List(10) { index ->
            Category(
                id = "$index",
                title = "title",
                description = "",
                imageUrl = "",
                questionSettings = emptyList(),
            )
        }

        categoryRepository.setCategories(categories)

        assertIs<CategoryUiState.Success>(viewModel.categoryUiState.value)
    }
}
