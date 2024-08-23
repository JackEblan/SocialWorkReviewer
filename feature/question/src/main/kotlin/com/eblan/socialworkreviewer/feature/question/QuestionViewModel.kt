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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.eblan.socialworkreviewer.core.data.repository.AverageRepository
import com.eblan.socialworkreviewer.core.data.repository.CategoryRepository
import com.eblan.socialworkreviewer.core.data.repository.ChoiceRepository
import com.eblan.socialworkreviewer.core.domain.GetQuestionsUseCase
import com.eblan.socialworkreviewer.core.domain.UpdateChoiceUseCase
import com.eblan.socialworkreviewer.core.model.Average
import com.eblan.socialworkreviewer.core.model.Choice
import com.eblan.socialworkreviewer.core.model.Question
import com.eblan.socialworkreviewer.core.model.QuestionData
import com.eblan.socialworkreviewer.core.model.QuestionSetting
import com.eblan.socialworkreviewer.feature.question.navigation.QuestionRouteData
import com.eblan.socialworkreviewer.framework.countdowntimer.CountDownTimerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val choiceRepository: ChoiceRepository,
    private val categoryRepository: CategoryRepository,
    private val averageRepository: AverageRepository,
    private val countDownTimerWrapper: CountDownTimerWrapper,
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val updateChoiceUseCase: UpdateChoiceUseCase,
) : ViewModel() {

    private val questionRouteData = savedStateHandle.toRoute<QuestionRouteData>()

    private val id = questionRouteData.id

    private val _questionUiState = MutableStateFlow<QuestionUiState?>(null)
    val questionUiState = _questionUiState.asStateFlow()

    val currentQuestionData = choiceRepository.currentQuestionData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = QuestionData(
            selectedChoices = emptyList(),
            questionsWithSelectedChoices = emptyMap(),
        ),
    )

    val countDownTime = countDownTimerWrapper.countDownTimeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    fun startQuestions(questionSettingIndex: Int, questionSetting: QuestionSetting) {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Loading
            }

            val questions = getQuestionsUseCase(
                id = id,
                numberOfQuestions = questionSetting.numberOfQuestions,
            )

            countDownTimerWrapper.setCountDownTimer(
                millisInFuture = questionSetting.minutes * 60000L,
                countDownInterval = 1000,
            )

            _questionUiState.update {
                QuestionUiState.Questions(
                    questionSettingIndex = questionSettingIndex,
                    questions = questions,
                )
            }

            countDownTimerWrapper.start()
        }
    }

    fun startQuickQuestions() {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Loading
            }

            _questionUiState.update {
                QuestionUiState.QuickQuestions(
                    questions = getQuestionsUseCase(id = id),
                )
            }
        }
    }

    fun updateChoice(choice: Choice) {
        viewModelScope.launch {
            updateChoiceUseCase(choice = choice)
        }
    }

    fun addCurrentQuestion(question: Question) {
        viewModelScope.launch {
            choiceRepository.addCurrentQuestion(question)
        }
    }

    fun showCorrectChoices(questions: List<Question>) {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.CorrectChoices(
                    questions = questions,
                )
            }
        }
    }

    fun showScore(questionSettingIndex: Int, questions: List<Question>) {
        viewModelScope.launch {
            val score = choiceRepository.getScore()

            _questionUiState.update {
                QuestionUiState.Score(
                    score = score,
                    questions = questions,
                    lastCountDownTime = countDownTime.value?.minutes,
                )
            }

            averageRepository.upsertAverage(
                Average(
                    questionSettingIndex = questionSettingIndex,
                    score = score,
                    numberOfQuestions = questions.size,
                    categoryId = id,
                ),
            )

            countDownTimerWrapper.cancel()
        }
    }

    fun getCategory() {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Loading
            }

            _questionUiState.update {
                QuestionUiState.OnBoarding(categoryRepository.getCategory(categoryId = id))
            }
        }
    }

    fun clearCache() {
        countDownTimerWrapper.cancel()

        choiceRepository.clearCache()
    }
}
