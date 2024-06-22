package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionDataSource {
    fun getQuestions(category: String): Flow<List<Question>>
}