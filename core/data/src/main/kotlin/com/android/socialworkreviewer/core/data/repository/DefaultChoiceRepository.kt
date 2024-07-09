package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.cache.InMemoryChoiceDataSource
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class DefaultChoiceRepository @Inject constructor(
    private val inMemoryChoiceDataSource: InMemoryChoiceDataSource
) : ChoiceRepository {

    override val selectedChoices get() = inMemoryChoiceDataSource.selectedChoices

    override val questionsWithSelectedChoicesFlow: SharedFlow<Map<Question, List<String>>>
        get() = inMemoryChoiceDataSource.questionsWithSelectedChoicesFlow

    override suspend fun addChoice(choice: Choice) {
        inMemoryChoiceDataSource.addChoice(choice)
    }

    override suspend fun deleteChoice(choice: Choice) {
        inMemoryChoiceDataSource.deleteChoice(choice)
    }

    override fun clearSelectedChoices() {
        inMemoryChoiceDataSource.clearSelectedChoices()
    }
}