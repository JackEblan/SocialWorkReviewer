package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Question

interface QuestionRepository {
    suspend fun getQuestions(id: String, numberOfQuestions: Int,): List<Question>
}