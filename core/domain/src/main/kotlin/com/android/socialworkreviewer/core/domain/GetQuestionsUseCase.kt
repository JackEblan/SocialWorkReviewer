package com.android.socialworkreviewer.core.domain

import com.android.socialworkreviewer.core.data.repository.QuestionRepository
import com.android.socialworkreviewer.core.model.Question
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    suspend operator fun invoke(
        id: String, numberOfQuestions: Int? = null
    ): List<Question> {
        return if (numberOfQuestions != null) {
            questionRepository.getQuestions(id = id).take(numberOfQuestions)
        } else {
            questionRepository.getQuestions(id = id)
        }
    }
}