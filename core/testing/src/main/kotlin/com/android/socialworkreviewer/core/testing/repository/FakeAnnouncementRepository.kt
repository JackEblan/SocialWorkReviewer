package com.android.socialworkreviewer.core.testing.repository

import com.android.socialworkreviewer.core.data.repository.AnnouncementRepository
import com.android.socialworkreviewer.core.model.Announcement
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class FakeAnnouncementRepository : AnnouncementRepository {
    private val _announcementsFlow =
        MutableSharedFlow<List<Announcement>>(replay = 1, onBufferOverflow = DROP_OLDEST)

    override fun getAnnouncements(): Flow<List<Announcement>> {
        return _announcementsFlow.asSharedFlow().distinctUntilChanged()
    }

    fun setAnnouncements(value: List<Announcement>) {
        _announcementsFlow.tryEmit(value)
    }
}