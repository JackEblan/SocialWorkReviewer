package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Announcement

@NoArg
@Keep
data class AnnouncementDocument(
    val id: String?,
    val orderNumber: Int?,
    val title: String?,
    val message: String?,
) {
    companion object {
        const val ORDER_NUMBER = "orderNumber"
    }
}

fun AnnouncementDocument.asExternalModel(): Announcement {
    return Announcement(
        id = id.toString(), orderNumber = orderNumber ?: 0,
        title = title.toString(),
        message = message.toString()
    )
}
