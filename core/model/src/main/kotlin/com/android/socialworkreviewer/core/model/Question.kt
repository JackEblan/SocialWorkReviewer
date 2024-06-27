package com.android.socialworkreviewer.core.model

data class Question(
    val question: String,
    val correctChoices: List<String>,
    val wrongChoices: List<String>,
    val choices: List<String> = correctChoices.plus(wrongChoices).shuffled()
)