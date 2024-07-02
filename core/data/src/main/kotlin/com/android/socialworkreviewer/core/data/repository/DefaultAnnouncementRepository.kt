package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Announcement
import com.android.socialworkreviewer.core.network.firestore.AnnouncementDataSource
import com.android.socialworkreviewer.core.network.model.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultAnnouncementRepository @Inject constructor(private val announcementDataSource: AnnouncementDataSource) :
    AnnouncementRepository {
    override fun getAnnouncement(): Flow<List<Announcement>> {
        return announcementDataSource.getAnnouncementDocument().distinctUntilChanged()
            .map { categoryDocuments ->
                categoryDocuments.map { categoryDocument -> categoryDocument.asExternalModel() }
            }
    }
}