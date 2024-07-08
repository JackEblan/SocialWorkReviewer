package com.android.socialworkreviewer.core.cache

import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.Flow

interface InMemoryChoiceDataSource {
    val selectedChoicesFlow: Flow<List<Choice>>

    val questionsWithSelectedChoicesFlow: Flow<Map<Question, List<String>>>

    val questions: List<Question>

    suspend fun addChoice(choice: Choice)

    suspend fun deleteChoice(choice: Choice)

    fun addQuestions(value: List<Question>)

    fun getSelectedChoices(): List<Choice>
}