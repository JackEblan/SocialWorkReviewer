package com.android.socialworkreviewer.feature.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.socialworkreviewer.core.data.repository.AnswerRepository
import com.android.socialworkreviewer.core.data.repository.QuestionRepository
import com.android.socialworkreviewer.core.model.Answer
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.feature.question.navigation.QuestionRouteData
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
    private val questionRepository: QuestionRepository,
    private val answerRepository: AnswerRepository
) : ViewModel() {

    private val questionRouteData = savedStateHandle.toRoute<QuestionRouteData>()

    private val id = questionRouteData.id

    private val _questionUiState = MutableStateFlow<QuestionUiState>(QuestionUiState.Loading)
    val questionUiState = _questionUiState.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    val answers = answerRepository.answers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun getQuestions() {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Success(
                    questions = questionRepository.getQuestions(
                        id = id
                    ),
                )
            }
        }
    }

    fun updateAnswer(answer: Answer) {
        viewModelScope.launch {
            answerRepository.updateAnswer(answer = answer)
        }
    }

    fun getScore() {
        viewModelScope.launch {
            _score.update {
                answerRepository.getScore()
            }
        }
    }

    fun addQuestions(value: List<Question>) {
        viewModelScope.launch {
            answerRepository.addQuestions(value = value)
        }
    }

    fun showAnswers() {
        _questionUiState.update {
            QuestionUiState.ShowAnswer(answerRepository.questions)
        }
    }
}