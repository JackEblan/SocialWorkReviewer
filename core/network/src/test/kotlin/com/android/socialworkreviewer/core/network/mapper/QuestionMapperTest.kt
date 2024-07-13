package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.network.model.QuestionDocument
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class QuestionMapperTest {

    @Test
    fun toQuestion() {
        val question = toQuestion(
            questionDocument = QuestionDocument(
                question = "question",
                correctChoices = listOf("A"),
                wrongChoices = listOf("B"),
            ),
        )

        assertEquals(expected = "question", actual = question.question)
        assertEquals(expected = listOf("A"), actual = question.correctChoices)
        assertEquals(expected = listOf("B"), actual = question.wrongChoices)
        assertEquals(expected = listOf("B"), actual = question.wrongChoices)
        assertTrue(question.choices.containsAll(listOf("A") + listOf("B")))
    }
}