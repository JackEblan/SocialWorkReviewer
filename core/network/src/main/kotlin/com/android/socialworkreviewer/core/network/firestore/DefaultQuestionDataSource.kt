package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.android.socialworkreviewer.core.network.firestore.QuestionDataSource.Companion.QUESTIONS_COLLECTION
import com.android.socialworkreviewer.core.network.model.QuestionDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultQuestionDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Dispatcher(SocialWorkReviewerDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : QuestionDataSource {
    override suspend fun getQuestions(id: String): List<QuestionDocument> {
        return withContext(ioDispatcher) {
            firestore.collection(CATEGORIES_COLLECTION).document(id)
                .collection(QUESTIONS_COLLECTION).get().await()
                .mapNotNull { queryDocumentSnapshot ->
                    try {
                        queryDocumentSnapshot.toObject()
                    } catch (e: RuntimeException) {
                        null
                    }
                }
        }
    }
}