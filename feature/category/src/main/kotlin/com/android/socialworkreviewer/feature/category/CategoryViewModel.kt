package com.android.socialworkreviewer.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.socialworkreviewer.core.domain.GetCategoriesAndAverageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(getCategoriesAndAverageUseCase: GetCategoriesAndAverageUseCase) :
    ViewModel() {

    private val _categoryErrorMessage = MutableStateFlow<String?>(null)

    val categoryErrorMessage = _categoryErrorMessage.asStateFlow()

    val categoryUiState =
        getCategoriesAndAverageUseCase().catch { throwable -> _categoryErrorMessage.update { throwable.message } }
            .map(CategoryUiState::Success).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CategoryUiState.Loading
            )
}