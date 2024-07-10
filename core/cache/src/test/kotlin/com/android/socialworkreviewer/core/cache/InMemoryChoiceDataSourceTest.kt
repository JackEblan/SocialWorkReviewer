package com.android.socialworkreviewer.core.cache

import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InMemoryChoiceDataSourceTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var inMemoryChoiceDataSource: InMemoryChoiceDataSource

    @Before
    fun setUp() {
        inMemoryChoiceDataSource =
            DefaultInMemoryChoiceDataSource(defaultDispatcher = testDispatcher)
    }

    @Test
    fun addChoice() = runTest {
        val choice = Choice(
            question = Question(
                question = "Question",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf("Choices")
            ), choice = "Choice"
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        assertTrue {
            inMemoryChoiceDataSource.selectedChoices.contains(choice)
        }

        assertEquals(
            actual = inMemoryChoiceDataSource.questionsWithSelectedChoicesFlow.first()[choice.question],
            expected = listOf("Choice")
        )
    }

    @Test
    fun deleteChoice() = runTest {
        val choice = Choice(
            question = Question(
                question = "Question",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf("Choices")
            ), choice = "Choice"
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        inMemoryChoiceDataSource.deleteChoice(choice = choice)

        assertTrue {
            inMemoryChoiceDataSource.selectedChoices.contains(choice).not()
        }

        assertTrue {
            inMemoryChoiceDataSource.questionsWithSelectedChoicesFlow.first().isEmpty()
        }
    }

    @Test
    fun clearCache() = runTest {
        val choice = Choice(
            question = Question(
                question = "Question",
                correctChoices = listOf(),
                wrongChoices = listOf(),
                choices = listOf("Choices")
            ), choice = "Choice"
        )

        inMemoryChoiceDataSource.addChoice(choice = choice)

        inMemoryChoiceDataSource.clearCache()

        assertTrue {
            inMemoryChoiceDataSource.selectedChoices.isEmpty()
        }

        assertTrue {
            inMemoryChoiceDataSource.questionsWithSelectedChoicesFlow.first().isEmpty()
        }
    }
}