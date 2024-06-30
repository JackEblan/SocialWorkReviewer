package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.network.model.QuestionDocument

interface QuestionDataSource {
    suspend fun getQuestions(id: String): List<QuestionDocument>

    companion object {
        const val QUESTIONS_COLLECTION = "questions"
    }
}