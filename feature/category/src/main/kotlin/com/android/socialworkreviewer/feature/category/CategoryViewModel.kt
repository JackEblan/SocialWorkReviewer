/*
 *
 *   Copyright 2023 Einstein Blanco
 *
 *   Licensed under the GNU General Public License v3.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
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
        announcementRepository.getAnnouncements(),
        getCategoriesAndAverageUseCase(),
    ) { announcements, categories ->
        CategoryUiState.Success(announcements = announcements, categories = categories)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategoryUiState.Loading,
    )
}
