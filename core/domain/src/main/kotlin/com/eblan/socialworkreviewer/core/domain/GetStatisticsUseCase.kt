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

import com.eblan.socialworkreviewer.core.data.repository.AverageRepository
import com.eblan.socialworkreviewer.core.model.Statistics
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetStatisticsUseCase @Inject constructor(private val averageRepository: AverageRepository) {
    suspend operator fun invoke(categoryId: String): Statistics {
        val totalScore = averageRepository.getAverageByCategory(categoryId = categoryId).first()
            .sumOf { averageEntity ->
                averageEntity.score
            }

        val totalNumberOfQuestions =
            averageRepository.getAverageByCategory(categoryId = categoryId).first()
                .sumOf { averageEntity ->
                    averageEntity.numberOfQuestions
                }

        val totalAverage = if (totalNumberOfQuestions == 0) {
            0.0
        } else {
            (totalScore.toDouble() / totalNumberOfQuestions.toDouble()) * 100.0
        }

        return Statistics(
            totalAverage = totalAverage,
            totalScore = totalScore,
            totalNumberOfQuestions = totalNumberOfQuestions,
        )
    }
}
