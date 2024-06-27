package com.android.socialworkreviewer.feature.question

import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.model.QuestionSetting

sealed interface QuestionUiState {
    data class Success(val questions: List<Question>) : QuestionUiState

    data object Loading : QuestionUiState

    data class ShowAnswers(val questions: List<Question>) : QuestionUiState

    data class QuestionSettings(val questionSettings: List<QuestionSetting>) : QuestionUiState
}