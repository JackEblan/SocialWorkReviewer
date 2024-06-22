package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Category
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultCategoryRepository @Inject constructor(private val categoryDataSource: CategoryDataSource) :
    CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return categoryDataSource.getCategories()
    }
}