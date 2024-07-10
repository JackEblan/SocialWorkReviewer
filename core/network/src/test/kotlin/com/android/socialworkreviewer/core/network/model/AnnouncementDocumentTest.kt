package com.android.socialworkreviewer.core.network.model

import com.google.firebase.Timestamp
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class AnnouncementDocumentTest {

    @Test
    fun asExternalModel() {
        val announcement = AnnouncementDocument(
            id = "id", date = Timestamp(date = Date()), title = "Title", message = "Message"
        ).asExternalModel()

        assertEquals(expected = "id", actual = announcement.id)
        assertEquals(expected = "Title", actual = announcement.title)
        assertEquals(expected = "Message", actual = announcement.message)
    }
}