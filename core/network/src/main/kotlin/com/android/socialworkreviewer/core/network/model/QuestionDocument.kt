package com.android.socialworkreviewer.core.network.model

@NoArg
data class QuestionDocument(
    val question: String,
    val correctChoices: List<String>,
    val wrongChoices: List<String>,
)
