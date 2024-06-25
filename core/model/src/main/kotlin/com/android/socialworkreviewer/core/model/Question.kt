package com.android.socialworkreviewer.core.model

@NoArg
data class Question(
    val question: String, val correctAnswers: List<Choice>, val wrongAnswers: List<Choice>
)