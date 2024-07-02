package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Announcement

@NoArg
@Keep
data class AnnouncementDocument(
    val id: String?,
    val priority: Int?,
    val title: String?,
    val message: String?,
)

fun AnnouncementDocument.asExternalModel(): Announcement {
    return Announcement(
        id = id.toString(),
        priority = priority ?: 0,
        title = title.toString(),
        message = message.toString()
    )
}
