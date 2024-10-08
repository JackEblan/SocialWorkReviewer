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
package com.eblan.socialworkreviewer.feature.question

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.eblan.socialworkreviewer.core.domain.GetQuestionsUseCase
import com.eblan.socialworkreviewer.core.domain.GetStatisticsUseCase
import com.eblan.socialworkreviewer.core.domain.UpdateChoiceUseCase
import com.eblan.socialworkreviewer.core.model.Average
import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionSetting
import com.eblan.socialworkreviewer.core.testing.countdowntimer.FakeCountDownTimer
import com.eblan.socialworkreviewer.core.testing.repository.FakeAverageRepository
import com.eblan.socialworkreviewer.core.testing.repository.FakeCategoryRepository
import com.eblan.socialworkreviewer.core.testing.repository.FakeChoiceRepository
import com.eblan.socialworkreviewer.core.testing.repository.FakeQuestionRepository
import com.eblan.socialworkreviewer.feature.question.navigation.QuestionRouteData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
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

    private lateinit var getStatisticsUseCase: GetStatisticsUseCase

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

        getStatisticsUseCase = GetStatisticsUseCase(averageRepository = averageRepository)

        viewModel = QuestionViewModel(
            savedStateHandle = savedStateHandle,
            choiceRepository = choiceRepository,
            categoryRepository = categoryRepository,
            averageRepository = averageRepository,
            countDownTimerWrapper = countDownTimerWrapper,
            getQuestionsUseCase = getQuestionsUseCase,
            updateChoiceUseCase = updateChoiceUseCase,
            getStatisticsUseCase = getStatisticsUseCase,
        )
    }

    @Test
    fun questionUiState_isNull_whenStarted() = runTest {
        assertNull(viewModel.questionUiState.value)
    }

    @Test
    fun countDownTime_isNull_whenStarted() = runTest {
        assertNull(viewModel.countDownTime.value)
    }

    @Test
    fun startQuestions() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.questionUiState.collect()
        }

        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.countDownTime.collect()
        }

        val questions = List(10) { _ ->
            Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(),
            )
        }

        questionRepository.addQuestions(questions)

        viewModel.startQuestions(
            questionSettingIndex = 1,
            questionSetting = QuestionSetting(
                numberOfQuestions = 1,
                minutes = 1,
            ),
        )

        assertIs<QuestionUiState.Questions>(viewModel.questionUiState.value)

        assertNotNull(viewModel.countDownTime.value)
    }

    @Test
    fun startQuickQuestions() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.questionUiState.collect()
        }

        val questions = List(10) { _ ->
            Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(),
            )
        }

        questionRepository.addQuestions(questions)

        viewModel.startQuickQuestions()

        assertIs<QuestionUiState.QuickQuestions>(viewModel.questionUiState.value)
    }

    @Test
    fun updateMultipleChoices() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.answeredQuestionsFlow.collect()
        }

        val question = Question(
            question = "",
            correctChoices = listOf("", ""),
            wrongChoices = listOf(),
            choices = listOf(),
        )

        repeat(2) {
            viewModel.updateChoice(
                question = question,
                choice = "$it",
            )
        }

        assertTrue {
            viewModel.answeredQuestionsFlow.value[question]?.size == 2
        }
    }

    @Test
    fun updateSingleChoice() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.answeredQuestionsFlow.collect()
        }

        val question = Question(
            question = "",
            correctChoices = listOf("", ""),
            wrongChoices = listOf(),
            choices = listOf(),
        )

        viewModel.updateChoice(
            question = question,
            choice = "",
        )

        assertTrue {
            viewModel.answeredQuestionsFlow.value[question]?.size == 1
        }
    }

    @Test
    fun showCorrectChoices() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.questionUiState.collect()
        }

        val questions = List(10) { _ ->
            Question(
                question = "",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf(),
            )
        }

        val averages = List(10) { _ ->
            Average(
                questionSettingIndex = 1,
                score = 10,
                numberOfQuestions = 10,
                categoryId = "id",
            )
        }

        questionRepository.addQuestions(questions)

        averageRepository.setAverages(value = averages)

        viewModel.showCorrectChoices(questionSettingIndex = 0, questions = questions)

        assertIs<QuestionUiState.CorrectChoices>(viewModel.questionUiState.value)
    }

    @Test
    fun getCategory() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.questionUiState.collect()
        }

        val categories = List(10) { index ->
            Category(
                id = "id",
                title = "Title $index",
                description = "description",
                imageUrl = "imageUrl",
                questionSettings = emptyList(),
            )
        }

        val averages = List(10) { _ ->
            Average(
                questionSettingIndex = 1,
                score = 10,
                numberOfQuestions = 10,
                categoryId = "id",
            )
        }

        categoryRepository.setCategories(value = categories)

        averageRepository.setAverages(value = averages)

        viewModel.getCategory()

        assertIs<QuestionUiState.OnBoarding>(viewModel.questionUiState.value)
    }
}
