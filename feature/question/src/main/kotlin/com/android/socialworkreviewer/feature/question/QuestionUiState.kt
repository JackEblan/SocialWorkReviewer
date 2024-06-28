package com.android.socialworkreviewer.feature.question

import com.android.socialworkreviewer.core.model.Category
import com.android.socialworkreviewer.core.model.Question

sealed interface QuestionUiState {
    data class Questions(val questions: List<Question>) : QuestionUiState

    data object Loading : QuestionUiState

    data class ShowCorrectChoices(val questions: List<Question>) : QuestionUiState

    data class OnBoarding(val category: Category? = null) : QuestionUiState
}