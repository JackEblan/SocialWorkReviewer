package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers
import com.android.socialworkreviewer.core.network.firestore.MessageDataSource.Companion.MESSAGES_COLLECTION
import com.android.socialworkreviewer.core.network.firestore.MessageDataSource.Companion.MESSAGES_DOCUMENT
import com.android.socialworkreviewer.core.network.model.MessageDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultMessageDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Dispatcher(SocialWorkReviewerDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : MessageDataSource {
    override suspend fun getMessage(): MessageDocument? {
        return withContext(ioDispatcher) {
            val documentSnapshot =
                firestore.collection(MESSAGES_COLLECTION).document(MESSAGES_DOCUMENT).get().await()

            try {
                documentSnapshot.toObject<MessageDocument>()
            } catch (e: RuntimeException) {
                null
            }
        }
    }
}