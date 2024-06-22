package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
}