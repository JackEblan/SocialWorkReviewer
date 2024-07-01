package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers.IO
import com.android.socialworkreviewer.core.database.dao.AverageDao
import com.android.socialworkreviewer.core.database.model.asEntity
import com.android.socialworkreviewer.core.database.model.asExternalModel
import com.android.socialworkreviewer.core.model.Average
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultAverageRepository @Inject constructor(
    private val averageDao: AverageDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : AverageRepository {
    override fun getAverageByCategory(categoryId: String): Flow<List<Average>> {
        return averageDao.getAverageEntitiesByCategory(categoryId = categoryId)
            .map { averageEntities ->
                averageEntities.map { averageEntity -> averageEntity.asExternalModel() }
            }
    }

    override suspend fun upsertAverage(average: Average) {
        withContext(ioDispatcher) {
            averageDao.upsertAverageEntity(average.asEntity())
        }
    }
}
