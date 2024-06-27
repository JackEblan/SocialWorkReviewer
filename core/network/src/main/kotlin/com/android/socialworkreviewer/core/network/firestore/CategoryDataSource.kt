package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.network.model.CategoryDocument
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {
    fun getCategoryDocuments(): Flow<List<CategoryDocument>>

    suspend fun getCategoryDocument(id: String): CategoryDocument?

    companion object {
        const val CATEGORIES_COLLECTION = "categories"
    }
}