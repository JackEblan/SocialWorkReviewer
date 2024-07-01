package com.android.socialworkreviewer.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.android.socialworkreviewer.core.database.model.AverageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AverageDao {
    @Upsert
    suspend fun upsertAverageEntity(entity: AverageEntity)

    @Query("SELECT * FROM AverageEntity WHERE categoryId = :categoryId")
    fun getAverageEntitiesByCategory(categoryId: String): Flow<List<AverageEntity>>
}