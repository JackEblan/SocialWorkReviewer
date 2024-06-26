package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SocialWorkReviewerDispatchers.Default
import com.android.socialworkreviewer.core.model.Answer
import com.android.socialworkreviewer.core.model.Question
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultAnswerRepository @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
) : AnswerRepository {

    private val _answers = MutableSharedFlow<List<Answer>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private var _questions = emptyList<Question>()

    private val currentAnswers get() = _answers.replayCache.firstOrNull() ?: emptyList()

    override val answers = _answers.asSharedFlow()

    override val questions get() = _questions

    override suspend fun getScore(): Int {
        return withContext(defaultDispatcher) {
            currentAnswers.groupBy({ it.question }, { it.choice })
                .count { it.value.containsAll(it.key.correctChoices) }
        }
    }

    override fun addQuestions(value: List<Question>) {
        _questions = value
    }

    override suspend fun updateAnswer(answer: Answer) {
        if (answer in currentAnswers) {
            _answers.emit((currentAnswers - answer))
        } else {
            _answers.emit((currentAnswers + answer))
        }
    }
}