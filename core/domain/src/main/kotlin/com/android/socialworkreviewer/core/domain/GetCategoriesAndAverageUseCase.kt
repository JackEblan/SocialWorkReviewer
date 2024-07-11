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
package com.android.socialworkreviewer.core.domain

import com.android.socialworkreviewer.core.data.repository.AverageRepository
import com.android.socialworkreviewer.core.data.repository.CategoryRepository
import com.android.socialworkreviewer.core.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesAndAverageUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val averageRepository: AverageRepository,
) {
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getCategories().map { categories ->
            categories.map { category ->
                val totalScore =
                    averageRepository.getAverageByCategory(categoryId = category.id).first()
                        .sumOf { averageEntity ->
                            averageEntity.score
                        }

                val totalNumberOfQuestions =
                    averageRepository.getAverageByCategory(categoryId = category.id).first()
                        .sumOf { averageEntity ->
                            averageEntity.numberOfQuestions
                        }

                val average = if (totalNumberOfQuestions == 0) {
                    0.0
                } else {
                    (totalScore.toDouble() / totalNumberOfQuestions.toDouble()) * 100.0
                }

                category.copy(average = average)
            }
        }
    }
}
