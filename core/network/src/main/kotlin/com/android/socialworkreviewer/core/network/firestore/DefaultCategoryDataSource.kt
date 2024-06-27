package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.android.socialworkreviewer.core.network.model.CategoryDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultCategoryDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Dispatcher(SocialWorkReviewerDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : CategoryDataSource {
    override fun getCategoryDocuments(): Flow<List<CategoryDocument>> {
        return firestore.collection(CATEGORIES_COLLECTION).snapshots()
            .mapNotNull { it.toObjects<CategoryDocument>() }
    }

    override suspend fun getCategoryDocument(id: String): CategoryDocument? {
        return withContext(ioDispatcher) {
            firestore.collection(CATEGORIES_COLLECTION).document(id).get().await()
                .toObject<CategoryDocument>()
        }
    }
}