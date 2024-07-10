package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.cache.InMemoryChoiceDataSource
import com.android.socialworkreviewer.core.model.Choice
import javax.inject.Inject

internal class DefaultChoiceRepository @Inject constructor(
    private val inMemoryChoiceDataSource: InMemoryChoiceDataSource
) : ChoiceRepository {

    override val selectedChoices get() = inMemoryChoiceDataSource.selectedChoices

    override val questionsWithSelectedChoicesFlow get() = inMemoryChoiceDataSource.questionsWithSelectedChoicesFlow

    override suspend fun addChoice(choice: Choice) {
        inMemoryChoiceDataSource.addChoice(choice)
    }

    override suspend fun deleteChoice(choice: Choice) {
        inMemoryChoiceDataSource.deleteChoice(choice)
    }

    override fun clearCache() {
        inMemoryChoiceDataSource.clearCache()
    }
}