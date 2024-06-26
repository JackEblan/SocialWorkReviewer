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

    private val _answersFlow = MutableSharedFlow<List<Answer>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private var _questions = emptyList<Question>()

    private val _answeredQuestionsFlow = MutableSharedFlow<Map<Question, List<String>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private val currentAnswers get() = _answersFlow.replayCache.firstOrNull() ?: emptyList()

    override val answersFlow = _answersFlow.asSharedFlow()

    override val questions get() = _questions

    override val answeredQuestionsFlow = _answeredQuestionsFlow.asSharedFlow()

    override fun addQuestions(value: List<Question>) {
        _questions = value
    }

    override suspend fun updateAnswer(answer: Answer) {
        if (answer in currentAnswers) {
            _answersFlow.emit((currentAnswers - answer))
        } else {
            _answersFlow.emit((currentAnswers + answer))
        }

        val answeredQuestions = withContext(defaultDispatcher) {
            currentAnswers.groupBy({ it.question }, { it.choice })
        }

        _answeredQuestionsFlow.emit(answeredQuestions)
    }
}