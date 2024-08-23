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
package com.eblan.socialworkreviewer.core.cache

import com.eblan.socialworkreviewer.core.common.Dispatcher
import com.eblan.socialworkreviewer.core.common.SwrDispatchers.Default
import com.eblan.socialworkreviewer.core.model.Choice
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultInMemoryChoiceDataSource @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher,
) : InMemoryChoiceDataSource {
    private val _selectedChoices = mutableListOf<Choice>()

    private val _currentQuestionData = MutableSharedFlow<QuestionData>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val selectedChoices get() = _selectedChoices.toList()

    override val currentQuestionData get() = _currentQuestionData.asSharedFlow()

    override suspend fun addChoice(choice: Choice) {
        _selectedChoices.add(choice)

        _currentQuestionData.emit(
            QuestionData(
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
                selectedChoices = getQuestionsWithSelectedChoices().getOrDefault(
                    key = choice.question,
                    defaultValue = emptyList(),
                ),
                questionsWithSelectedChoices = getQuestionsWithSelectedChoices(),
            ),
        )
    }

    override fun clearCache() {
        _selectedChoices.clear()

        _currentQuestionData.resetReplayCache()
    }

    override suspend fun addCurrentQuestion(question: Question) {
        _currentQuestionData.emit(
            QuestionData(
                selectedChoices = getQuestionsWithSelectedChoices().getOrDefault(
                    key = question,
                    defaultValue = emptyList(),
                ),
                questionsWithSelectedChoices = getQuestionsWithSelectedChoices(),
            ),
        )
    }

    override suspend fun getScore(): Int {
        return withContext(defaultDispatcher) {
            getQuestionsWithSelectedChoices().count {
                it.value.toSet() == it.key.correctChoices.toSet()
            }
        }
    }

    private suspend fun getQuestionsWithSelectedChoices(): Map<Question, List<String>> {
        return withContext(defaultDispatcher) {
            _selectedChoices.groupBy({ it.question }, { it.choice })
        }
    }
}
