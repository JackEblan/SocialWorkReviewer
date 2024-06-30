package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.network.firestore.QuestionDataSource
import javax.inject.Inject

internal class DefaultQuestionRepository @Inject constructor(private val questionDataSource: QuestionDataSource) :
    QuestionRepository {
    override suspend fun getQuestions(id: String): List<Question> {
        return questionDataSource.getQuestions(id = id).map { questionDocument ->
            Question(
                question = questionDocument.question,
                correctChoices = questionDocument.correctChoices,
                wrongChoices = questionDocument.wrongChoices,
                choices = questionDocument.correctChoices.plus(questionDocument.wrongChoices)
                    .shuffled()
            )

        }.shuffled()
    }
}