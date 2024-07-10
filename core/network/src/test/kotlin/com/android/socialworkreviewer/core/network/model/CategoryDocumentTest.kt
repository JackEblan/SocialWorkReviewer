package com.android.socialworkreviewer.core.network.model

import com.android.socialworkreviewer.core.model.QuestionSetting
import com.google.firebase.Timestamp
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class CategoryDocumentTest {

    @Test
    fun asExternalModel() {
        val category = CategoryDocument(
            id = "id",
            date = Timestamp(date = Date()),
            title = "title",
            description = "description",
            imageUrl = "imageUrl",
            questionSettings = listOf(
                QuestionSettingDocument(
                    numberOfQuestions = 10, minutes = 10
                )
            )
        ).asExternalModel()

        assertEquals(expected = "id", actual = category.id)
        assertEquals(expected = "title", actual = category.title)
        assertEquals(expected = "description", actual = category.description)
        assertEquals(expected = "imageUrl", actual = category.imageUrl)
        assertEquals(
            expected = listOf(
                QuestionSetting(
                    numberOfQuestions = 10, minutes = 10
                )
            ), actual = category.questionSettings
        )
    }
}