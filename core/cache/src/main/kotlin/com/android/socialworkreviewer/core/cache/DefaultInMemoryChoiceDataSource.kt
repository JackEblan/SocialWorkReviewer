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

    private val _selectedChoicesFlow = MutableSharedFlow<List<Choice>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private val _questionsWithSelectedChoicesFlow = MutableSharedFlow<Map<Question, List<String>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private var _questions = emptyList<Question>()

    private val _selectedChoices
        get() = _selectedChoicesFlow.replayCache.firstOrNull() ?: emptyList()

    override val selectedChoicesFlow = _selectedChoicesFlow.asSharedFlow()

    override val questions get() = _questions

    override val questionsWithSelectedChoicesFlow = _questionsWithSelectedChoicesFlow.asSharedFlow()

    override fun addQuestions(value: List<Question>) {
        _questions = value
    }

    override suspend fun addChoice(choice: Choice) {
        _selectedChoicesFlow.emit(_selectedChoices + choice)

        val questionsWithSelectedChoices = withContext(defaultDispatcher) {
            _selectedChoices.groupBy({ it.question }, { it.choice })
        }

        _questionsWithSelectedChoicesFlow.emit(questionsWithSelectedChoices)
    }

    override suspend fun deleteChoice(choice: Choice) {
        _selectedChoicesFlow.emit(_selectedChoices - choice)

        val questionsWithSelectedChoices = withContext(defaultDispatcher) {
            _selectedChoices.groupBy({ it.question }, { it.choice })
        }

        _questionsWithSelectedChoicesFlow.emit(questionsWithSelectedChoices)
    }

    override fun getSelectedChoices(): List<Choice> {
        return _selectedChoices
    }
}