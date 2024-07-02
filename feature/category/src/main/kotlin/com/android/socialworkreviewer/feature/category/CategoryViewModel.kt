package com.android.socialworkreviewer.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.socialworkreviewer.core.data.repository.AnnouncementRepository
import com.android.socialworkreviewer.core.domain.GetCategoriesAndAverageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    getCategoriesAndAverageUseCase: GetCategoriesAndAverageUseCase,
    announcementRepository: AnnouncementRepository,
) : ViewModel() {
    val categoryUiState = combine(
        announcementRepository.getAnnouncement(), getCategoriesAndAverageUseCase()
    ) { announcements, categories ->
        CategoryUiState.Success(announcements = announcements, categories = categories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryUiState.Loading
    )
}