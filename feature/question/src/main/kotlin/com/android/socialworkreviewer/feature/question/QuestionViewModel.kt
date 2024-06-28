package com.android.socialworkreviewer.feature.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.socialworkreviewer.core.data.repository.CategoryRepository
import com.android.socialworkreviewer.core.data.repository.ChoiceRepository
import com.android.socialworkreviewer.core.data.repository.QuestionRepository
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.feature.question.navigation.QuestionRouteData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val questionRepository: QuestionRepository,
    private val choiceRepository: ChoiceRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val questionRouteData = savedStateHandle.toRoute<QuestionRouteData>()

    private val id = questionRouteData.id

    private val _questionUiState = MutableStateFlow<QuestionUiState?>(null)
    val questionUiState = _questionUiState.asStateFlow()

    val scoreCount = choiceRepository.answeredQuestionsFlow.map { answeredQuestions ->
        answeredQuestions.count {
            it.value.containsAll(it.key.correctChoices)
        }
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = 0
    )

    val answeredQuestionsCount =
        choiceRepository.answeredQuestionsFlow.map { answeredQuestions -> answeredQuestions.size }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    private val _currentQuestion = MutableStateFlow<Question?>(null)

    val selectedChoices = combine(
        _currentQuestion, choiceRepository.choicesFlow
    ) { question, answer ->
        answer.filter { it.question == question }.map { it.choice }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun getQuestions(numberOfQuestions: Int, minutes: Int) {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Loading
            }

            _questionUiState.update {
                QuestionUiState.Questions(
                    questions = questionRepository.getQuestions(
                        id = id, numberOfQuestions = numberOfQuestions
                    ),
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

    fun addQuestions(value: List<Question>) {
        viewModelScope.launch {
            choiceRepository.addQuestions(value = value)
        }
    }

    fun showCorrectChoices() {
        _questionUiState.update {
            QuestionUiState.ShowCorrectChoices(choiceRepository.questions)
        }
    }

    fun getCategory() {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Loading
            }

            _questionUiState.update {
                QuestionUiState.OnBoarding(categoryRepository.getCategory(id = id))
            }
        }
    }
}