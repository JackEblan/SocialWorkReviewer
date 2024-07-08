package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.cache.InMemoryChoiceDataSource
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import javax.inject.Inject

internal class DefaultChoiceRepository @Inject constructor(
    private val inMemoryChoiceDataSource: InMemoryChoiceDataSource
) : ChoiceRepository {

    override val selectedChoicesFlow = inMemoryChoiceDataSource.selectedChoicesFlow

    override val questions get() = inMemoryChoiceDataSource.questions

    override val questionsWithSelectedChoicesFlow =
        inMemoryChoiceDataSource.questionsWithSelectedChoicesFlow

    override fun addQuestions(questions: List<Question>) {
        inMemoryChoiceDataSource.addQuestions(questions)
    }

    override suspend fun addChoice(choice: Choice) {
        inMemoryChoiceDataSource.addChoice(choice)
    }

    override suspend fun deleteChoice(choice: Choice) {
        inMemoryChoiceDataSource.deleteChoice(choice)
    }

    override fun getSelectedChoices(): List<Choice> {
        return inMemoryChoiceDataSource.getSelectedChoices()
    }
}