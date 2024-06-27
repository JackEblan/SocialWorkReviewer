package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.network.firestore.QuestionDataSource
import com.android.socialworkreviewer.core.network.model.asExternalModel
import javax.inject.Inject

internal class DefaultQuestionRepository @Inject constructor(private val questionDataSource: QuestionDataSource) :
    QuestionRepository {
    override suspend fun getQuestions(id: String, numberOfQuestions: Int): List<Question> {
        return questionDataSource.getQuestions(id = id, numberOfQuestions = numberOfQuestions)
            .map { it.asExternalModel() }.shuffled()
    }
}