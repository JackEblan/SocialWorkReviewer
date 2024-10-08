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
package com.eblan.socialworkreviewer.core.testing.repository

import com.eblan.socialworkreviewer.core.data.repository.NewsRepository
import com.eblan.socialworkreviewer.core.model.News
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeNewsRepository : NewsRepository {
    private val _newsFlow =
        MutableSharedFlow<List<News>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    override fun getNews(): Flow<List<News>> {
        return _newsFlow
    }

    fun setNews(value: List<News>) {
        _newsFlow.tryEmit(value)
    }
}
