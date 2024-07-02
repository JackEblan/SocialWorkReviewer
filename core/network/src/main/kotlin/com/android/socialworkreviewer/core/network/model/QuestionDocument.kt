package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Question

@NoArg
@Keep
data class QuestionDocument(
    val question: String?,
    val correctChoices: List<String>?,
    val wrongChoices: List<String>?,
)

fun QuestionDocument.asExternalModel(): Question {
    return Question(
        question = question.toString(),
        correctChoices = correctChoices ?: emptyList(),
        wrongChoices = wrongChoices ?: emptyList(),
        choices = correctChoices?.plus(wrongChoices ?: emptyList())?.shuffled() ?: emptyList()
    )
}
