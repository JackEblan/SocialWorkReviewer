package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Answer
import kotlinx.coroutines.flow.Flow

interface AnswerRepository {
    val answers: Flow<List<Answer>>

    suspend fun addAnswer(answer: Answer)

    suspend fun removeAnswer(answer: Answer)

    suspend fun getScore(): Int
}