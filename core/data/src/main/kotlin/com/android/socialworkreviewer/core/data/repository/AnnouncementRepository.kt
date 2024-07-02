package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun getAnnouncement(): Flow<List<Announcement>>
}