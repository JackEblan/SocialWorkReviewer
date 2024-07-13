package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.model.QuestionSetting
import com.android.socialworkreviewer.core.network.model.CategoryDocument
import com.android.socialworkreviewer.core.network.model.QuestionSettingDocument
import com.google.firebase.Timestamp
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class CategoryMapperTest {

    @Test
    fun toCategory() {
        val category = toCategory(
            categoryDocument = CategoryDocument(
                id = "id",
                date = Timestamp(date = Date()),
                title = "title",
                description = "description",
                imageUrl = "imageUrl",
                questionSettings = listOf(
                    QuestionSettingDocument(
                        numberOfQuestions = 10,
                        minutes = 10,
                    ),
                ),
            ),
        )

        assertEquals(expected = "id", actual = category.id)
        assertEquals(expected = "title", actual = category.title)
        assertEquals(expected = "description", actual = category.description)
        assertEquals(expected = "imageUrl", actual = category.imageUrl)
        assertEquals(
            expected = listOf(
                QuestionSetting(
                    numberOfQuestions = 10,
                    minutes = 10,
                ),
            ),
            actual = category.questionSettings,
        )
    }
}