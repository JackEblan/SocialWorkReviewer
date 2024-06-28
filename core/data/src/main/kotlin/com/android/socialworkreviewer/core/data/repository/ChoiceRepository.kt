package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.Flow

interface ChoiceRepository {
    val choicesFlow: Flow<List<Choice>>

    val answeredQuestionsFlow: Flow<Map<Question, List<String>>>

    val questions: List<Question>

    suspend fun updateChoice(choice: Choice)

    fun addQuestions(value: List<Question>)
}