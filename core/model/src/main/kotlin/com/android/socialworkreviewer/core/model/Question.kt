package com.android.socialworkreviewer.core.model

@NoArg
data class Question(
    val question: String,
    val correctChoices: List<String>,
    val wrongChoices: List<String>,
)