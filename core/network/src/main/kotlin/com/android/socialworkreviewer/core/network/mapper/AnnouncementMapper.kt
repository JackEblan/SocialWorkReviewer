package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.model.Announcement
import com.android.socialworkreviewer.core.network.model.AnnouncementDocument

internal fun toAnnouncement(announcementDocument: AnnouncementDocument): Announcement {
    val id = announcementDocument.id.toString()

    val title = announcementDocument.title.toString()

    val message = announcementDocument.message.toString()

    return Announcement(
        id = id,
        title = title,
        message = message,
    )
}
