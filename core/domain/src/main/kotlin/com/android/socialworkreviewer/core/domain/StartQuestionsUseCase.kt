package com.android.socialworkreviewer.core.domain

import com.android.socialworkreviewer.core.data.repository.ChoiceRepository
import com.android.socialworkreviewer.core.data.repository.QuestionRepository
import com.android.socialworkreviewer.core.model.Question
import javax.inject.Inject

class StartQuestionsUseCase @Inject constructor(
    private val choiceRepository: ChoiceRepository,
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(
        id: String, numberOfQuestions: Int? = null
    ): List<Question> {

        val questions = if (numberOfQuestions != null) {
            questionRepository.getQuestions(id = id).take(numberOfQuestions)
        } else {
            questionRepository.getQuestions(id = id)
        }

        choiceRepository.clearCache()

        choiceRepository.addQuestions(questions = questions)

        return questions
    }
}