package com.android.socialworkreviewer.core.cache

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers.Default
import com.android.socialworkreviewer.core.model.Choice
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultInMemoryChoiceDataSource @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
) : InMemoryChoiceDataSource {
    private val _selectedChoices = mutableListOf<Choice>()

    private val _questionsWithSelectedChoicesFlow = MutableSharedFlow<Map<Question, List<String>>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val selectedChoices get() = _selectedChoices.toList()

    override val questionsWithSelectedChoicesFlow = _questionsWithSelectedChoicesFlow.asSharedFlow()

    override suspend fun addChoice(choice: Choice) {
        _selectedChoices.add(choice)

        _questionsWithSelectedChoicesFlow.emit(getQuestionsWithSelectedChoices())
    }

    override suspend fun deleteChoice(choice: Choice) {
        _selectedChoices.remove(choice)

        _questionsWithSelectedChoicesFlow.emit(getQuestionsWithSelectedChoices())
    }

    override suspend fun clearCache() {
        _selectedChoices.clear()

        _questionsWithSelectedChoicesFlow.resetReplayCache()

        _questionsWithSelectedChoicesFlow.emit(emptyMap())
    }

    private suspend fun getQuestionsWithSelectedChoices(): Map<Question, List<String>> {
        return withContext(defaultDispatcher) {
            _selectedChoices.groupBy({ it.question }, { it.choice })
        }
    }
}