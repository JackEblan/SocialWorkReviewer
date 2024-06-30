package com.android.socialworkreviewer.core.data.repository

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

internal class DefaultChoiceRepository @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
) : ChoiceRepository {

    private val _choicesFlow = MutableSharedFlow<List<Choice>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private val _answeredQuestionsFlow = MutableSharedFlow<Map<Question, List<String>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private var _questions = emptyList<Question>()

    private val selectedChoices get() = _choicesFlow.replayCache.firstOrNull() ?: emptyList()

    override val choicesFlow = _choicesFlow.asSharedFlow()

    override val questions get() = _questions

    override val answeredQuestionsFlow = _answeredQuestionsFlow.asSharedFlow()

    override fun addQuestions(value: List<Question>) {
        _questions = value
    }

    override suspend fun updateChoice(choice: Choice) {
        if (choice in selectedChoices) {
            _choicesFlow.emit(selectedChoices - choice)
        } else {
            _choicesFlow.emit(selectedChoices + choice)
        }

        val answeredQuestions = withContext(defaultDispatcher) {
            selectedChoices.groupBy({ it.question }, { it.choice })
        }

        _answeredQuestionsFlow.emit(answeredQuestions)
    }
}