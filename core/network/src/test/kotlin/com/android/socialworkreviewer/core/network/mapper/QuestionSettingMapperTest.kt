package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.network.model.QuestionSettingDocument
import org.junit.Test
import kotlin.test.assertEquals

class QuestionSettingMapperTest {

    @Test
    fun toQuestionSetting() {
        val questionSetting = toQuestionSetting(
            questionSettingDocument = QuestionSettingDocument(
                numberOfQuestions = 10,
                minutes = 10,
            ),
        )

        assertEquals(expected = 10, actual = questionSetting.numberOfQuestions)
        assertEquals(expected = 10, actual = questionSetting.minutes)
    }
}