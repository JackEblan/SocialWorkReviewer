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
package com.android.socialworkreviewer.core.domain

import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.testing.repository.FakeChoiceRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class UpdateChoiceUseCaseTest {
    private lateinit var choiceRepository: FakeChoiceRepository

    private lateinit var updateChoiceUseCase: UpdateChoiceUseCase

    @Before
    fun setup() {
        choiceRepository = FakeChoiceRepository()

        updateChoiceUseCase = UpdateChoiceUseCase(choiceRepository = choiceRepository)
    }

    @Test
    fun insertSingleChoice() = runTest {
        val question = Question(
            question = "question",
            correctChoices = listOf(""),
            wrongChoices = listOf(""),
            choices = emptyList(),
        )

        repeat(2) { index ->
            val choice = Choice(
                question = question,
                choice = "$index",
            )

            updateChoiceUseCase(
                choice = choice,
            )
        }

        assertTrue {
            choiceRepository.selectedChoices.size == 1
        }
    }

    @Test
    fun insertMultipleChoice() = runTest {
        val question = Question(
            question = "",
            correctChoices = listOf("", ""),
            wrongChoices = listOf(""),
            choices = listOf(""),
        )

        repeat(2) { index ->
            val choice = Choice(
                question = question,
                choice = "$index",
            )

            updateChoiceUseCase(
                choice = choice,
            )
        }

        assertTrue {
            choiceRepository.selectedChoices.size == 2
        }
    }
}
