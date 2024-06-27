package com.android.socialworkreviewer.core.network.model

import com.android.socialworkreviewer.core.model.Question

@NoArg
data class QuestionDocument(
    val question: String,
    val correctChoices: List<String>,
    val wrongChoices: List<String>,
)

fun QuestionDocument.asExternalModel(): Question {
    return Question(
        question = question,
        correctChoices = correctChoices,
        wrongChoices = wrongChoices,
    )
}
