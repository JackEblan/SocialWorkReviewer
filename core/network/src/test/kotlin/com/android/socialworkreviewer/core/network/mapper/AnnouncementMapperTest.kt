package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.network.model.AnnouncementDocument
import com.google.firebase.Timestamp
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class AnnouncementMapperTest {

    @Test
    fun toAnnouncement() {
        val announcement = toAnnouncement(
            announcementDocument = AnnouncementDocument(
                id = "id",
                date = Timestamp(date = Date()),
                title = "Title",
                message = "Message",
            ),
        )

        assertEquals(expected = "id", actual = announcement.id)
        assertEquals(expected = "Title", actual = announcement.title)
        assertEquals(expected = "Message", actual = announcement.message)
    }
}