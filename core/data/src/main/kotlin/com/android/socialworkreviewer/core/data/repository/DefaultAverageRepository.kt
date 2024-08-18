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
package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SwrDispatchers.IO
import com.android.socialworkreviewer.core.database.dao.AverageDao
import com.android.socialworkreviewer.core.database.model.AverageEntity
import com.android.socialworkreviewer.core.model.Average
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultAverageRepository @Inject constructor(
    private val averageDao: AverageDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : AverageRepository {
    override fun getAverageByCategory(categoryId: String): Flow<List<Average>> {
        return averageDao.getAverageEntitiesByCategory(categoryId = categoryId)
            .map { averageEntities ->
                averageEntities.map { averageEntity -> averageEntity.toAverage() }
            }.distinctUntilChanged()
    }

    override suspend fun upsertAverage(average: Average) {
        withContext(ioDispatcher) {
            averageDao.upsertAverageEntity(average.toAverageEntity())
        }
    }

    private fun AverageEntity.toAverage(): Average {
        return Average(
            questionSettingIndex = questionSettingIndex,
            score = score,
            numberOfQuestions = numberOfQuestions,
            categoryId = categoryId,
        )
    }

    private fun Average.toAverageEntity(): AverageEntity {
        return AverageEntity(
            questionSettingIndex = questionSettingIndex,
            score = score,
            numberOfQuestions = numberOfQuestions,
            categoryId = categoryId,
        )
    }
}
