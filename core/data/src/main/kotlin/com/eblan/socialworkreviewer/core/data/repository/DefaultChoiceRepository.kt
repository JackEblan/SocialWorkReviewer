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
import com.eblan.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class DefaultChoiceRepository @Inject constructor(
    private val inMemoryChoiceDataSource: InMemoryChoiceDataSource,
) : ChoiceRepository {
    override val answeredQuestionsFlow: SharedFlow<Map<Question, List<String>>>
        get() = inMemoryChoiceDataSource.answeredQuestionsFlow

    override fun multipleChoices(question: Question, choice: String) {
        inMemoryChoiceDataSource.multipleChoices(question = question, choice = choice)
    }

    override fun singleChoice(question: Question, choice: String) {
        inMemoryChoiceDataSource.singleChoice(question = question, choice = choice)
    }

    override fun clearCache() {
        inMemoryChoiceDataSource.clearCache()
    }
}
