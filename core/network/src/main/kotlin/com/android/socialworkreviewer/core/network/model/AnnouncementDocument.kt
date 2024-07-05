package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Announcement
import com.google.firebase.Timestamp

@NoArg
@Keep
data class AnnouncementDocument(
    val id: String?,
    val date: Timestamp?,
    val title: String?,
    val message: String?,
) {
    companion object {
        const val DATE = "date"
    }
}

fun AnnouncementDocument.asExternalModel(): Announcement {
    return Announcement(
        id = id.toString(), title = title.toString(), message = message.toString()
    )
}
