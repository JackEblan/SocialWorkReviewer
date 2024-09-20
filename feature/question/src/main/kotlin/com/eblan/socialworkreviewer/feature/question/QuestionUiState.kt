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
package com.eblan.socialworkreviewer.feature.question

import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.Statistics

sealed interface QuestionUiState {
    data class Questions(val questionSettingIndex: Int, val questions: List<Question>) :
        QuestionUiState

    data object Loading : QuestionUiState

    data class CorrectChoices(
        val questions: List<Question>,
        val score: Int,
        val lastCountDownTime: String?,
    ) : QuestionUiState

    data class OnBoarding(val category: Category? = null, val statistics: Statistics) :
        QuestionUiState

    data class QuickQuestions(val questions: List<Question>) : QuestionUiState
}
