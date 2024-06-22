package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.model.Category
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultCategoryDataSource @Inject constructor(private val firestore: FirebaseFirestore) :
    CategoryDataSource {
    override fun getCategories(): Flow<List<Category>> {
        return firestore.collection(CATEGORIES_COLLECTION).snapshots()
            .map { it.toObjects<Category>() }
    }
}