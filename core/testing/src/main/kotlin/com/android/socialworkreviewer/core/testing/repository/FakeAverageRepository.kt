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
package com.android.socialworkreviewer.core.testing.repository

import com.android.socialworkreviewer.core.data.repository.AverageRepository
import com.android.socialworkreviewer.core.model.Average
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class FakeAverageRepository : AverageRepository {
    private val _averagesFlow =
        MutableSharedFlow<List<Average>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val _currentAverages get() = _averagesFlow.replayCache.firstOrNull() ?: emptyList()

    override fun getAverageByCategory(categoryId: String): Flow<List<Average>> {
        return _averagesFlow.map { averages ->
            averages.filter { average ->
                average.categoryId == categoryId
            }
        }
    }

    override suspend fun upsertAverage(average: Average) {
        _averagesFlow.tryEmit((_currentAverages + average).distinct())
    }

    fun setAverages(value: List<Average>) {
        _averagesFlow.tryEmit(value)
    }
}
