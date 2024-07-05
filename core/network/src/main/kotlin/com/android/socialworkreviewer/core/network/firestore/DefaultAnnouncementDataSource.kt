package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.network.firestore.AnnouncementDataSource.Companion.ANNOUNCEMENTS_COLLECTION
import com.android.socialworkreviewer.core.network.model.AnnouncementDocument
import com.android.socialworkreviewer.core.network.model.AnnouncementDocument.Companion.ORDER_NUMBER
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class DefaultAnnouncementDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) : AnnouncementDataSource {
    override fun getAnnouncementDocument(): Flow<List<AnnouncementDocument>> {
        return firestore.collection(ANNOUNCEMENTS_COLLECTION)
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
}