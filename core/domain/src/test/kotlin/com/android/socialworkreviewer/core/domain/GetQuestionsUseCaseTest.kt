package com.android.socialworkreviewer.core.domain

import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.testing.repository.FakeQuestionRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class GetQuestionsUseCaseTest {
    private lateinit var questionRepository: FakeQuestionRepository

    private lateinit var getQuestionsUseCase: GetQuestionsUseCase

    @Before
    fun setup() {
        questionRepository = FakeQuestionRepository()

        getQuestionsUseCase = GetQuestionsUseCase(questionRepository = questionRepository)
    }

    @Test
    fun takeNumberOfQuestions() = runTest {
        val numberOfQuestionsToTake = 10

        questionRepository.setQuestions(
            List(20) { _ ->
                Question(
                    question = "",
                    correctChoices = listOf(),
                    wrongChoices = listOf(),
                    choices = listOf(),
                )
            },
        )

        assertTrue {
            getQuestionsUseCase(
                id = "",
                numberOfQuestions = numberOfQuestionsToTake,
            ).size == numberOfQuestionsToTake
        }
    }

    @Test
    fun getQuestions() = runTest {
        questionRepository.setQuestions(
            List(20) { _ ->
                Question(
                    question = "",
                    correctChoices = listOf(),
                    wrongChoices = listOf(),
                    choices = listOf(),
                )
            },
        )

        assertTrue {
            getQuestionsUseCase(
                id = "",
                numberOfQuestions = null,
            ).size == questionRepository.getQuestions("").size
        }
    }
}