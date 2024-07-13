package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.network.model.QuestionDocument

internal fun toQuestion(questionDocument: QuestionDocument): Question {
    val question = questionDocument.question.toString()

    val correctChoices = questionDocument.correctChoices ?: emptyList()

    val wrongChoices = questionDocument.wrongChoices ?: emptyList()

    val choices = correctChoices.plus(wrongChoices).shuffled()

    return Question(
        question = question,
        correctChoices = correctChoices,
        wrongChoices = wrongChoices,
        choices = choices,
    )
}
