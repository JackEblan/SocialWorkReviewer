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

import com.eblan.socialworkreviewer.core.model.Question
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class InMemoryChoiceDataSourceTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var inMemoryChoiceDataSource: InMemoryChoiceDataSource

    @Before
    fun setup() {
        inMemoryChoiceDataSource =
            DefaultInMemoryChoiceDataSource(defaultDispatcher = testDispatcher)
    }

    @Test
    fun multipleChoices() = runTest {
        val question = Question(
            question = "",
            correctChoices = listOf(),
            wrongChoices = listOf(),
            choices = listOf(""),
        )

        repeat(2) { index ->
            inMemoryChoiceDataSource.multipleChoices(question = question, choice = "$index")
        }

        val answeredQuestion = inMemoryChoiceDataSource.answeredQuestionsFlow.replayCache.first()

        assertTrue {
            answeredQuestion[question]?.size == 2
        }
    }

    @Test
    fun singleChoice() = runTest {
        val question = Question(
            question = "",
            correctChoices = listOf(),
            wrongChoices = listOf(),
            choices = listOf(""),
        )

        inMemoryChoiceDataSource.singleChoice(question = question, choice = "")

        val answeredQuestion = inMemoryChoiceDataSource.answeredQuestionsFlow.replayCache.first()

        assertTrue {
            answeredQuestion[question]?.size == 1
        }
    }

    @Test
    fun getScore() = runTest {
        repeat(10) { index ->
            inMemoryChoiceDataSource.singleChoice(
                question = Question(
                    question = "$index",
                    correctChoices = listOf(""),
                    wrongChoices = listOf(),
                    choices = listOf(""),
                ),
                choice = "",
            )
        }

        assertTrue {
            inMemoryChoiceDataSource.getScore() == 10
        }
    }

    @Test
    fun clearCache() = runTest {
        repeat(10) { index ->
            val question = Question(
                question = "$index",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(""),
            )

            inMemoryChoiceDataSource.singleChoice(question = question, choice = "$index")
        }

        inMemoryChoiceDataSource.clearCache()

        assertTrue {
            inMemoryChoiceDataSource.answeredQuestionsFlow.replayCache.isEmpty()
        }
    }
}
