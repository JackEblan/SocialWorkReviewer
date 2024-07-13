package com.android.socialworkreviewer.core.testing.repository

import com.android.socialworkreviewer.core.data.repository.QuestionRepository
import com.android.socialworkreviewer.core.model.Question

class FakeQuestionRepository : QuestionRepository {
    private var _questions = emptyList<Question>()

    override suspend fun getQuestions(id: String): List<Question> {
        return _questions
    }

    fun setQuestions(value: List<Question>) {
        _questions = value
    }
}