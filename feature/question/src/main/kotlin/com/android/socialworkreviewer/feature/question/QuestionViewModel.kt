package com.android.socialworkreviewer.feature.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.android.socialworkreviewer.core.data.repository.QuestionRepository
import com.android.socialworkreviewer.feature.question.navigation.QuestionRouteData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val questionRepository: QuestionRepository
) : ViewModel() {

    private val questionRouteData = savedStateHandle.toRoute<QuestionRouteData>()

    private val id = questionRouteData.id

    private val _questionUiState = MutableStateFlow<QuestionUiState>(QuestionUiState.OnBoarding)
    val questionUiState = _questionUiState.asStateFlow()

    fun getQuestions() {
        viewModelScope.launch {
            _questionUiState.update {
                QuestionUiState.Success(
                    questions = questionRepository.getQuestions(
                        id = id
                    )
                )
            }
        }
    }
}