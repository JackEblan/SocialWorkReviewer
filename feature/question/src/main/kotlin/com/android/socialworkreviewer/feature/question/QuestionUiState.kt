package com.android.socialworkreviewer.feature.question

import com.android.socialworkreviewer.core.model.Question

sealed interface QuestionUiState {
    data class Success(val questions: List<Question>) : QuestionUiState

    data object Loading : QuestionUiState

    data class ShowAnswer(val questions: List<Question>) : QuestionUiState
}