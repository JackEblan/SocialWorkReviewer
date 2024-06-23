package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.model.Question

interface QuestionDataSource {
    suspend fun getQuestions(id: String): List<Question>

    companion object {
        const val QUESTIONS_COLLECTION = "questions"
    }
}