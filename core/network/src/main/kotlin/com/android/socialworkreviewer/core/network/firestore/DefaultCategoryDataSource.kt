package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers.IO
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.android.socialworkreviewer.core.network.model.CategoryDocument
import com.android.socialworkreviewer.core.network.model.CategoryDocument.Companion.ORDER_NUMBER
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultCategoryDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CategoryDataSource {
    override fun getCategoryDocuments(): Flow<List<CategoryDocument>> {
        return firestore.collection(CATEGORIES_COLLECTION)
            .orderBy(ORDER_NUMBER, Query.Direction.ASCENDING).snapshots()
            .mapNotNull { querySnapshots ->
                querySnapshots.mapNotNull { queryDocumentSnapshot ->
                    try {
                        queryDocumentSnapshot.toObject()
                    } catch (e: RuntimeException) {
                        null
                    }
                }
            }
    }

    override suspend fun getCategoryDocument(categoryDocumentId: String): CategoryDocument? {
        return withContext(ioDispatcher) {
            val documentSnapshot =
                firestore.collection(CATEGORIES_COLLECTION).document(categoryDocumentId).get()
                    .await()

            try {
                documentSnapshot.toObject<CategoryDocument>()
            } catch (e: RuntimeException) {
                null
            }
        }
    }
}