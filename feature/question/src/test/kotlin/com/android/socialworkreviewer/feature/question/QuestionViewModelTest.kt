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
package com.android.socialworkreviewer.feature.question

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.android.socialworkreviewer.core.domain.GetQuestionsUseCase
import com.android.socialworkreviewer.core.domain.UpdateChoiceUseCase
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.model.QuestionData
import com.android.socialworkreviewer.core.model.QuestionSetting
import com.android.socialworkreviewer.core.testing.countdowntimer.FakeCountDownTimer
import com.android.socialworkreviewer.core.testing.repository.FakeAverageRepository
import com.android.socialworkreviewer.core.testing.repository.FakeCategoryRepository
import com.android.socialworkreviewer.core.testing.repository.FakeChoiceRepository
import com.android.socialworkreviewer.core.testing.repository.FakeQuestionRepository
import com.android.socialworkreviewer.feature.question.navigation.QuestionRouteData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class QuestionViewModelTest {
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var choiceRepository: FakeChoiceRepository

    private lateinit var categoryRepository: FakeCategoryRepository

    private lateinit var averageRepository: FakeAverageRepository

    private lateinit var questionRepository: FakeQuestionRepository

    private lateinit var countDownTimerWrapper: FakeCountDownTimer

    private lateinit var getQuestionsUseCase: GetQuestionsUseCase

    private lateinit var updateChoiceUseCase: UpdateChoiceUseCase

    private lateinit var viewModel: QuestionViewModel

    @Before
    fun setup() {
        savedStateHandle = SavedStateHandle(
            route = QuestionRouteData(id = "id"),
        )

        choiceRepository = FakeChoiceRepository()

        categoryRepository = FakeCategoryRepository()

        averageRepository = FakeAverageRepository()

        questionRepository = FakeQuestionRepository()

        countDownTimerWrapper = FakeCountDownTimer()

        getQuestionsUseCase = GetQuestionsUseCase(questionRepository = questionRepository)

        updateChoiceUseCase = UpdateChoiceUseCase(choiceRepository = choiceRepository)

        viewModel = QuestionViewModel(
            savedStateHandle = savedStateHandle,
            choiceRepository = choiceRepository,
            categoryRepository = categoryRepository,
            averageRepository = averageRepository,
            countDownTimerWrapper = countDownTimerWrapper,
            getQuestionsUseCase = getQuestionsUseCase,
            updateChoiceUseCase = updateChoiceUseCase,
        )
    }

    @Test
    fun questionUiState_isNull_whenStarted() = runTest {
        assertNull(viewModel.questionUiState.value)
    }

    @Test
    fun currentQuestionData_isEmpty_whenStarted() = runTest {
        assertEquals(
            expected = QuestionData(
                selectedChoices = emptyList(),
                questionsWithSelectedChoicesSize = 0,
            ),
            actual = viewModel.currentQuestionData.value,
        )
    }

    @Test
    fun countDownTime_isNull_whenStarted() = runTest {
        assertNull(viewModel.countDownTime.value)
    }

    @Test
    fun startQuestions() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.questionUiState.collect() }

        val collectJob2 = launch(UnconfinedTestDispatcher()) { viewModel.countDownTime.collect() }

        val questions = List(10) { _ ->
            Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(),
            )
        }
        questionRepository.setQuestions(questions)

        viewModel.startQuestions(
            questionSettingIndex = 1,
            questionSetting = QuestionSetting(
                numberOfQuestions = 1,
                minutes = 1,
            ),
        )

        assertIs<QuestionUiState.Questions>(viewModel.questionUiState.value)

        assertNotNull(viewModel.countDownTime.value)

        collectJob.cancel()

        collectJob2.cancel()
    }

    @Test
    fun startQuickQuestions() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.questionUiState.collect() }

        val questions = List(10) { _ ->
            Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(),
            )
        }
        questionRepository.setQuestions(questions)

        viewModel.startQuickQuestions()

        assertIs<QuestionUiState.QuickQuestions>(viewModel.questionUiState.value)

        collectJob.cancel()
    }

    @Test
    fun addCurrentQuestion() = runTest {
        val collectJob =
            launch(UnconfinedTestDispatcher()) { viewModel.currentQuestionData.collect() }

        val question = Question(
            question = "",
            correctChoices = listOf(""),
            wrongChoices = listOf(""),
            choices = listOf(""),
        )

        val choice = Choice(
            question = question,
            choice = "",
        )

        viewModel.addCurrentQuestion(question = question)

        viewModel.updateChoice(choice = choice)

        assertTrue(viewModel.currentQuestionData.value.selectedChoices.isNotEmpty())

        assertTrue(viewModel.currentQuestionData.value.questionsWithSelectedChoicesSize != 0)

        collectJob.cancel()
    }

    @Test
    fun showCorrectChoices() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.questionUiState.collect() }

        val questions = List(10) { _ ->
            Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(),
            )
        }
        questionRepository.setQuestions(questions)

        viewModel.showCorrectChoices(questionSettingIndex = 1, questions = questions)

        assertIs<QuestionUiState.ShowCorrectChoices>(viewModel.questionUiState.value)

        collectJob.cancel()
    }

    @Test
    fun getCategory() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.questionUiState.collect() }

        val questions = List(10) { _ ->
            Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(),
            )
        }
        questionRepository.setQuestions(questions)

        viewModel.getCategory()

        assertIs<QuestionUiState.OnBoarding>(viewModel.questionUiState.value)

        collectJob.cancel()
    }
}
