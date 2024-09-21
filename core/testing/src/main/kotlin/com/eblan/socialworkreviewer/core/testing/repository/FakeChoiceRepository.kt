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
package com.eblan.socialworkreviewer.core.testing.repository

import com.eblan.socialworkreviewer.core.data.repository.ChoiceRepository
import com.eblan.socialworkreviewer.core.model.Choice
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeChoiceRepository : ChoiceRepository {
    private val _questions = mutableListOf<Question>()

    private val _selectedChoices = mutableListOf<Choice>()

    private val _questionsWithSelectedChoicesFlow = MutableSharedFlow<Map<Question, List<String>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private val _currentQuestionData = MutableSharedFlow<QuestionData>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val selectedChoices get() = _selectedChoices.toList()

    override val currentQuestionData = _currentQuestionData.asSharedFlow()

    override fun addQuestions(questions: List<Question>) {
        _questions.addAll(questions)
    }

    override suspend fun addChoice(choice: Choice) {
        _selectedChoices.add(choice)

        _currentQuestionData.emit(
            QuestionData(
                question = choice.question,
                selectedChoices = getQuestionsWithSelectedChoices().getOrDefault(
                    key = choice.question,
                    defaultValue = emptyList(),
                ),
                questionsWithSelectedChoices = getQuestionsWithSelectedChoices(),
            ),
        )
    }

    override suspend fun deleteChoice(choice: Choice) {
        _selectedChoices.remove(choice)

        _currentQuestionData.emit(
            QuestionData(
                question = choice.question,
                selectedChoices = getQuestionsWithSelectedChoices().getOrDefault(
                    key = choice.question,
                    defaultValue = emptyList(),
                ),
                questionsWithSelectedChoices = getQuestionsWithSelectedChoices(),
            ),
        )
    }

    override suspend fun replaceChoice(oldChoice: Choice, newChoice: Choice) {
        val oldChoiceIndex = _selectedChoices.indexOf(oldChoice)

        if (oldChoiceIndex != -1) {
            _selectedChoices[oldChoiceIndex] = newChoice

            _currentQuestionData.emit(
                QuestionData(
                    question = newChoice.question,
                    selectedChoices = getQuestionsWithSelectedChoices().getOrDefault(
                        key = newChoice.question,
                        defaultValue = emptyList(),
                    ),
                    questionsWithSelectedChoices = getQuestionsWithSelectedChoices(),
                ),
            )
        }
    }

    override fun clearCache() {
        _selectedChoices.clear()

        _questionsWithSelectedChoicesFlow.resetReplayCache()
    }

    override suspend fun addCurrentQuestion(question: Question) {
        _currentQuestionData.emit(
            QuestionData(
                question = question,
                selectedChoices = getQuestionsWithSelectedChoices().getOrDefault(
                    key = question,
                    defaultValue = emptyList(),
                ),
                questionsWithSelectedChoices = getQuestionsWithSelectedChoices(),
            ),
        )
    }

    private fun getQuestionsWithSelectedChoices(): Map<Question, List<String>> {
        return _selectedChoices.groupBy({ it.question }, { it.choice })
    }
}
