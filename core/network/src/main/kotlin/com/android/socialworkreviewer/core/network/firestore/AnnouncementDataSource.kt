package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.network.model.AnnouncementDocument
import kotlinx.coroutines.flow.Flow

interface AnnouncementDataSource {
    fun getAnnouncementDocument(): Flow<List<AnnouncementDocument>>

    companion object {
        const val ANNOUNCEMENTS_COLLECTION = "announcements"
    }
}