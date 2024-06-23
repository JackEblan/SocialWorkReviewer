package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.network.firestore.QuestionDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultQuestionRepository @Inject constructor(private val questionDataSource: QuestionDataSource) :
    QuestionRepository {
    override fun getQuestions(id: String): Flow<List<Question>> {
        return questionDataSource.getQuestions(id = id)
    }
}