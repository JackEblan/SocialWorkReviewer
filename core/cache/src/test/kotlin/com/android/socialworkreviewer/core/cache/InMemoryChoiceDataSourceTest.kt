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
package com.android.socialworkreviewer.core.cache

import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
    fun addChoice() = runTest {
        val choice = Choice(
            question = Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(""),
            ),
            choice = "",
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        assertTrue {
            inMemoryChoiceDataSource.selectedChoices.contains(choice)
        }

        assertNotNull(inMemoryChoiceDataSource.currentQuestionData.replayCache.firstOrNull())
    }

    @Test
    fun deleteChoice() = runTest {
        val choice = Choice(
            question = Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(""),
            ),
            choice = "",
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        inMemoryChoiceDataSource.deleteChoice(choice = choice)

        assertTrue {
            inMemoryChoiceDataSource.selectedChoices.contains(choice).not()
        }

        assertNotNull(inMemoryChoiceDataSource.currentQuestionData.replayCache.firstOrNull())
    }

    @Test
    fun clearCache() = runTest {
        val choice = Choice(
            question = Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(""),
            ),
            choice = "",
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        inMemoryChoiceDataSource.clearCache()

        assertTrue {
            inMemoryChoiceDataSource.selectedChoices.isEmpty()
        }

        assertTrue {
            inMemoryChoiceDataSource.currentQuestionData.replayCache.isEmpty()
        }
    }

    @Test
    fun addQuestion() = runTest {
        val question = Question(
            question = "",
            correctChoices = listOf(),
            wrongChoices = listOf(),
            choices = listOf(""),
        )

        val choice = Choice(
            question = question,
            choice = "",
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        inMemoryChoiceDataSource.addCurrentQuestion(question = question)

        assertNotNull(inMemoryChoiceDataSource.currentQuestionData.replayCache.firstOrNull())
    }

    @Test
    fun getScore() = runTest {
        val question = Question(
            question = "Question",
            correctChoices = listOf(""),
            wrongChoices = listOf(),
            choices = listOf(""),
        )

        val choice = Choice(
            question = question,
            choice = "",
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        inMemoryChoiceDataSource.addCurrentQuestion(question = question)

        assertEquals(expected = 1, actual = inMemoryChoiceDataSource.getScore())
    }
}
