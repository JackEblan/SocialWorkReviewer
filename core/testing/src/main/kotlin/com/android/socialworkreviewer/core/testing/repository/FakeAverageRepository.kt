package com.android.socialworkreviewer.core.testing.repository

import com.android.socialworkreviewer.core.data.repository.AverageRepository
import com.android.socialworkreviewer.core.model.Average
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class FakeAverageRepository : AverageRepository {
    private val _averagesFlow =
        MutableSharedFlow<List<Average>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val _currentAverages get() = _averagesFlow.replayCache.firstOrNull() ?: emptyList()

    override fun getAverageByCategory(categoryId: String): Flow<List<Average>> {
        return _averagesFlow.asSharedFlow().map { averages ->
                averages.filter { average ->
                    average.categoryId == categoryId
                }
            }.distinctUntilChanged()
    }

    override suspend fun upsertAverage(average: Average) {
        _averagesFlow.tryEmit((_currentAverages + average).distinct())
    }
}