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

import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.testing.repository.FakeQuestionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class GetQuestionsUseCaseTest {
    private lateinit var questionRepository: FakeQuestionRepository

    private lateinit var getQuestionsUseCase: GetQuestionsUseCase

    @Before
    fun setup() {
        questionRepository = FakeQuestionRepository()

        getQuestionsUseCase = GetQuestionsUseCase(questionRepository = questionRepository)
    }

    @Test
    fun takeNumberOfQuestions() = runTest {
        val numberOfQuestionsToTake = 10

        questionRepository.addQuestions(
            List(20) { _ ->
                Question(
                    question = "",
                    correctChoices = listOf(),
                    wrongChoices = listOf(),
                    choices = listOf(),
                )
            },
        )

        assertTrue {
            getQuestionsUseCase(
                id = "",
                numberOfQuestions = numberOfQuestionsToTake,
            ).size == numberOfQuestionsToTake
        }
    }

    @Test
    fun getQuestions() = runTest {
        questionRepository.addQuestions(
            List(20) { _ ->
                Question(
                    question = "",
                    correctChoices = listOf(),
                    wrongChoices = listOf(),
                    choices = listOf(),
                )
            },
        )

        assertTrue {
            getQuestionsUseCase(
                id = "",
                numberOfQuestions = null,
            ).size == questionRepository.getQuestions("").size
        }
    }
}
