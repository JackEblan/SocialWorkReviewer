package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Answer
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.Flow

interface AnswerRepository {
    val answers: Flow<List<Answer>>

    val questions: List<Question>

    suspend fun updateAnswer(answer: Answer)

    suspend fun getScore(): Int

    fun addQuestions(value: List<Question>)
}