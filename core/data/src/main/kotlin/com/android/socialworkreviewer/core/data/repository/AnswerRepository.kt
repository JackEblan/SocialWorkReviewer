package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Answer
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.Flow

interface AnswerRepository {
    val answersFlow: Flow<List<Answer>>

    val answeredQuestionsFlow: Flow<Map<Question, List<String>>>

    val questions: List<Question>

    suspend fun updateAnswer(answer: Answer)

    fun addQuestions(value: List<Question>)
}