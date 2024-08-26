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
package com.eblan.socialworkreviewer.feature.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eblan.socialworkreviewer.core.data.repository.AboutRepository
import com.eblan.socialworkreviewer.framework.linkparser.LinkParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    aboutRepository: AboutRepository,
    private val linkParser: LinkParser,
) : ViewModel() {
    val aboutUiState = aboutRepository.getAbouts().map(AboutUiState::Success).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AboutUiState.Loading,
    )

    fun openLink(url: String) {
        linkParser.openLink(url = url)
    }
}
