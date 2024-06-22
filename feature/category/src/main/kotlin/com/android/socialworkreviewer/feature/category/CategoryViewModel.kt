package com.android.socialworkreviewer.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.socialworkreviewer.core.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) :
    ViewModel() {

    val categoryUiState = categoryRepository.getCategories().map(CategoryUiState::Success).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryUiState.Loading
    )
}