package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Average
import kotlinx.coroutines.flow.Flow

interface AverageRepository {
    fun getAverageByCategory(categoryId: String): Flow<List<Average>>

    suspend fun upsertAverage(average: Average)
}