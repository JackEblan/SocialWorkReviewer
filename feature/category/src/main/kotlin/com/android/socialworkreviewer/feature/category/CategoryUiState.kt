package com.android.socialworkreviewer.feature.category

import com.android.socialworkreviewer.core.model.Category

sealed interface CategoryUiState {
    data class Success(val categories: List<Category>) : CategoryUiState

    data object Loading : CategoryUiState
}