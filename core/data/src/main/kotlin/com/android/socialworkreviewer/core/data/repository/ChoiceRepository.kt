package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.SharedFlow

interface ChoiceRepository {
    val selectedChoices: List<Choice>

    val questionsWithSelectedChoicesFlow: SharedFlow<Map<Question, List<String>>>

    suspend fun addChoice(choice: Choice)

    suspend fun deleteChoice(choice: Choice)

    fun clearCache()
}