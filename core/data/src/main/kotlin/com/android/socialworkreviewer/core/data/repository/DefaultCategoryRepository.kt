package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Category
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource
import com.android.socialworkreviewer.core.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultCategoryRepository @Inject constructor(private val categoryDataSource: CategoryDataSource) :
    CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return categoryDataSource.getCategoryDocuments().distinctUntilChanged()
            .map { categoryDocuments ->
                categoryDocuments.map { categoryDocument -> categoryDocument.asExternalModel() }
            }
    }

    override suspend fun getCategory(categoryId: String): Category? {
        return categoryDataSource.getCategoryDocument(categoryDocumentId = categoryId)
            ?.asExternalModel()
    }
}