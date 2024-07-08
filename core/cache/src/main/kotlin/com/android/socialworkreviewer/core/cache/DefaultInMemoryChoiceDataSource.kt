package com.android.socialworkreviewer.core.cache

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers.Default
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultInMemoryChoiceDataSource @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
) : InMemoryChoiceDataSource {
    private var _questionsWithSelectedChoices = emptyMap<Question, List<String>>()

    private var _questions = emptyList<Question>()

    private val _selectedChoices = mutableListOf<Choice>()

    override val selectedChoices get() = _selectedChoices.toList()

    override val questions get() = _questions

    override val questionsWithSelectedChoices get() = _questionsWithSelectedChoices

    override fun addQuestions(value: List<Question>) {
        _questions = value
    }

    override suspend fun addChoice(choice: Choice) {
        _selectedChoices.add(choice)

        _questionsWithSelectedChoices = withContext(defaultDispatcher) {
            _selectedChoices.groupBy({ it.question }, { it.choice })
        }
    }

    override suspend fun deleteChoice(choice: Choice) {
        _selectedChoices.remove(choice)

        _questionsWithSelectedChoices = withContext(defaultDispatcher) {
            _selectedChoices.groupBy({ it.question }, { it.choice })
        }
    }

    override fun clearCache() {
        _questionsWithSelectedChoices = emptyMap()

        _questions = emptyList()

        _selectedChoices.clear()
    }
}