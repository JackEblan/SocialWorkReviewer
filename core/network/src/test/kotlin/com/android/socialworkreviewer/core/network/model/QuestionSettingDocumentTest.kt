package com.android.socialworkreviewer.core.network.model

import org.junit.Test
import kotlin.test.assertEquals

class QuestionSettingDocumentTest {

    @Test
    fun asExternalModel() {
        val questionSetting =
            QuestionSettingDocument(numberOfQuestions = 10, minutes = 10).asExternalModel()

        assertEquals(expected = 10, actual = questionSetting.numberOfQuestions)
        assertEquals(expected = 10, actual = questionSetting.minutes)
    }
}