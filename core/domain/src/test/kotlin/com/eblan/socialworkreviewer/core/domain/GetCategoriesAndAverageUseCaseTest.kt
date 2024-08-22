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
package com.eblan.socialworkreviewer.core.domain

import com.eblan.socialworkreviewer.core.model.Average
import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.testing.repository.FakeAverageRepository
import com.eblan.socialworkreviewer.core.testing.repository.FakeCategoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class GetCategoriesAndAverageUseCaseTest {
    private lateinit var categoryRepository: FakeCategoryRepository

    private lateinit var averageRepository: FakeAverageRepository

    private lateinit var getCategoriesAndAverageUseCase: GetCategoriesAndAverageUseCase

    @Before
    fun setup() {
        categoryRepository = FakeCategoryRepository()

        averageRepository = FakeAverageRepository()

        getCategoriesAndAverageUseCase = GetCategoriesAndAverageUseCase(
            categoryRepository = categoryRepository,
            averageRepository = averageRepository,
        )
    }

    @Test
    fun allCategoriesWithAverage() = runTest {
        repeat(10) { index ->
            averageRepository.upsertAverage(
                average = Average(
                    questionSettingIndex = 1,
                    score = 10,
                    numberOfQuestions = 10,
                    categoryId = "$index",
                ),
            )
        }

        categoryRepository.setCategories(
            List(10) { index ->
                Category(
                    id = "$index",
                    title = "Title $index",
                    description = "Description $index",
                    imageUrl = "",
                    average = 0.0,
                    questionSettings = emptyList(),
                )
            },
        )

        assertTrue {
            getCategoriesAndAverageUseCase().first().all { category ->
                category.average == 100.0
            }
        }
    }

    @Test
    fun allCategoriesWithoutAverage() = runTest {
        repeat(10) { index ->
            averageRepository.upsertAverage(
                average = Average(
                    questionSettingIndex = 1,
                    score = 0,
                    numberOfQuestions = 0,
                    categoryId = "$index",
                ),
            )
        }

        categoryRepository.setCategories(
            List(10) { index ->
                Category(
                    id = "$index",
                    title = "Title $index",
                    description = "Description $index",
                    imageUrl = "",
                    average = 0.0,
                    questionSettings = emptyList(),
                )
            },
        )

        assertTrue {
            getCategoriesAndAverageUseCase().first().all { category ->
                category.average == 0.0
            }
        }
    }

    @Test
    fun specificCategoryWithAverage() = runTest {
        averageRepository.upsertAverage(
            average = Average(
                questionSettingIndex = 1,
                score = 10,
                numberOfQuestions = 10,
                categoryId = "0",
            ),
        )

        categoryRepository.setCategories(
            List(10) { index ->
                Category(
                    id = "$index",
                    title = "Title $index",
                    description = "Description $index",
                    imageUrl = "",
                    average = 0.0,
                    questionSettings = emptyList(),
                )
            },
        )

        assertTrue {
            getCategoriesAndAverageUseCase().first().first().average == 100.0
        }
    }
}
