package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    fun getQuestions(id: String): Flow<List<Question>>
}