package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionDataSource {
    fun getQuestions(id: String): Flow<List<Question>>

    companion object {
        const val QUESTIONS_COLLECTION = "questions"
    }
}