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
import com.eblan.socialworkreviewer.core.model.Question
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultInMemoryChoiceDataSource @Inject constructor(@Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher) :
    InMemoryChoiceDataSource {
    private val _answeredQuestions = mutableMapOf<Question, List<String>>()

    private val _answeredQuestionsFlow = MutableSharedFlow<Map<Question, List<String>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val answeredQuestionsFlow get() = _answeredQuestionsFlow.asSharedFlow()

    override fun multipleChoices(question: Question, choice: String) {
        _answeredQuestions[question] = _answeredQuestions[question]?.let { selectedChoices ->
            if (choice in selectedChoices) {
                selectedChoices.minus(choice)
            } else {
                selectedChoices.plus(choice).distinct()
            }
        } ?: listOf(choice)

        _answeredQuestionsFlow.tryEmit(_answeredQuestions.toMap())
    }

    override fun singleChoice(question: Question, choice: String) {
        _answeredQuestions[question] = listOf(choice)

        _answeredQuestionsFlow.tryEmit(_answeredQuestions.toMap())
    }

    override suspend fun getScore(): Int {
        return withContext(defaultDispatcher) {
            _answeredQuestions.count {
                it.key.correctChoices == it.value
            }
        }
    }

    override fun clearCache() {
        _answeredQuestions.clear()

        _answeredQuestionsFlow.resetReplayCache()
    }
}
