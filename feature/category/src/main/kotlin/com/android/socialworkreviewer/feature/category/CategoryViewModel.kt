package com.android.socialworkreviewer.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.socialworkreviewer.core.data.repository.MessageRepository
import com.android.socialworkreviewer.core.domain.GetCategoriesAndAverageUseCase
import com.android.socialworkreviewer.core.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    getCategoriesAndAverageUseCase: GetCategoriesAndAverageUseCase,
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _message = MutableStateFlow<Message?>(null)

    val message = _message.asStateFlow()

    val categoryUiState = getCategoriesAndAverageUseCase().map(CategoryUiState::Success).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryUiState.Loading
    )

    fun getMessage() {
        viewModelScope.launch {
            _message.update {
                messageRepository.getMessage()
            }
        }
    }
}