package com.android.socialworkreviewer.core.network.model

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class QuestionDocumentTest {

    @Test
    fun asExternalModel() {
        val question = QuestionDocument(
            question = "question", correctChoices = listOf("A"), wrongChoices = listOf("B")
        ).asExternalModel()

        assertEquals(expected = "question", actual = question.question)
        assertEquals(expected = listOf("A"), actual = question.correctChoices)
        assertEquals(expected = listOf("B"), actual = question.wrongChoices)
        assertEquals(expected = listOf("B"), actual = question.wrongChoices)
        assertTrue(question.choices.containsAll(listOf("A") + listOf("B")))
    }
}