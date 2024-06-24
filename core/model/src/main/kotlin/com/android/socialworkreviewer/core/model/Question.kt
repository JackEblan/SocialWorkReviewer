package com.android.socialworkreviewer.core.model

@NoArg
data class Question(
    val question: String, val choices: List<Choice>, val answersId: List<String>
)