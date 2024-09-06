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

import com.eblan.socialworkreviewer.core.cache.InMemoryChoiceDataSource
import com.eblan.socialworkreviewer.core.model.Choice
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionData
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class DefaultChoiceRepository @Inject constructor(
    private val inMemoryChoiceDataSource: InMemoryChoiceDataSource,
) : ChoiceRepository {

    override val selectedChoices get() = inMemoryChoiceDataSource.selectedChoices

    override val currentQuestionData: SharedFlow<QuestionData>
        get() = inMemoryChoiceDataSource.currentQuestionData

    override fun addQuestions(questions: List<Question>) {
        inMemoryChoiceDataSource.addQuestions(questions = questions)
    }

    override suspend fun addChoice(choice: Choice) {
        inMemoryChoiceDataSource.addChoice(choice)
    }

    override suspend fun deleteChoice(choice: Choice) {
        inMemoryChoiceDataSource.deleteChoice(choice)
    }

    override fun clearCache() {
        inMemoryChoiceDataSource.clearCache()
    }

    override suspend fun addCurrentQuestion(question: Question) {
        inMemoryChoiceDataSource.addCurrentQuestion(question)
    }

    override suspend fun getScore(): Int {
        return inMemoryChoiceDataSource.getScore()
    }
}
