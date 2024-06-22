package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {
    fun getCategories(): Flow<List<Category>>

    companion object {
        const val CATEGORIES_COLLECTION = "categories"
    }
}