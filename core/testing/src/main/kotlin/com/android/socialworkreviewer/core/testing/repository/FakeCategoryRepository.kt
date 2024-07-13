package com.android.socialworkreviewer.core.testing.repository

import com.android.socialworkreviewer.core.data.repository.CategoryRepository
import com.android.socialworkreviewer.core.model.Category
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class FakeCategoryRepository : CategoryRepository {
    private val _categoriesFlow =
        MutableSharedFlow<List<Category>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val _currentCategories get() = _categoriesFlow.replayCache.firstOrNull() ?: emptyList()

    override fun getCategories(): Flow<List<Category>> {
        return _categoriesFlow.asSharedFlow().distinctUntilChanged()
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return _currentCategories.find { category -> category.id == categoryId }
    }

    fun setCategories(value: List<Category>) {
        _categoriesFlow.tryEmit(value)
    }
}