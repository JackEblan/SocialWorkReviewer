package com.android.socialworkreviewer.feature.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.socialworkreviewer.core.data.repository.AverageRepository
import com.android.socialworkreviewer.core.data.repository.CategoryRepository
import com.android.socialworkreviewer.core.data.repository.ChoiceRepository
import com.android.socialworkreviewer.core.domain.GetQuestionsUseCase
import com.android.socialworkreviewer.core.model.Average
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.model.QuestionSetting
import com.android.socialworkreviewer.feature.question.navigation.QuestionRouteData
import com.android.socialworkreviewer.framework.countdowntimer.CountDownTimerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
) : ViewModel() {

    private val questionRouteData = savedStateHandle.toRoute<QuestionRouteData>()

    private val id = questionRouteData.id

    private val _questionUiState = MutableStateFlow<QuestionUiState?>(null)
    val questionUiState = _questionUiState.asStateFlow()

    private val _countDownTimeSelected = MutableStateFlow<Int?>(null)

    private val _currentQuestion = MutableStateFlow<Question?>(null)

    val scoreCount =
        choiceRepository.questionsWithSelectedChoicesFlow.map { questionWithSelectedChoicesFlow ->
            questionWithSelectedChoicesFlow.count {
                it.value.containsAll(it.key.correctChoices)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val questionsWithSelectedChoicesSize =
        choiceRepository.questionsWithSelectedChoicesFlow.map { questionWithSelectedChoicesFlow -> questionWithSelectedChoicesFlow.size }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val selectedChoices = combine(
        _currentQuestion, choiceRepository.selectedChoicesFlow
    ) { question, choices ->
        choices.filter { it.question == question }.map { it.choice }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val countDownTimeUntilFinished =
        _countDownTimeSelected.filterNotNull().flatMapLatest { millisInFuture ->
            countDownTimerWrapper.getCountDownTime(
                millisInFuture = millisInFuture * 60000L,
                countDownInterval = 1000,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    val countDownTimerFinished = countDownTimerWrapper.countDownTimerFinished.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun getQuestions(questionSettingIndex: Int, questionSetting: QuestionSetting) {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Loading
            }

            val questions = getQuestionsUseCase(
                id = id, numberOfQuestions = questionSetting.numberOfQuestions
            )

            choiceRepository.addQuestions(value = questions)

            _countDownTimeSelected.update { questionSetting.minutes }

            _questionUiState.update {
                QuestionUiState.Questions(
                    questionSettingIndex = questionSettingIndex,
                    questions = questions,
                )
            }
        }
    }

    fun getQuickQuestions() {
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
            choiceRepository.updateChoice(choice = choice)
        }
    }

    fun addCurrentQuestion(question: Question) {
        _currentQuestion.update { question }
    }

    fun showCorrectChoices(questionSettingIndex: Int) {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.ShowCorrectChoices(
                    questions = choiceRepository.questions,
                    lastCountDownTime = countDownTimeUntilFinished.first()
                )
            }

            averageRepository.upsertAverage(
                Average(
                    questionSettingIndex = questionSettingIndex,
                    score = scoreCount.value,
                    numberOfQuestions = choiceRepository.questions.size,
                    categoryId = id
                )
            )
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

    fun startCountDownTimer() {
        countDownTimerWrapper.start()
    }

    fun cancelCountDownTimer() {
        countDownTimerWrapper.cancel()
    }
}