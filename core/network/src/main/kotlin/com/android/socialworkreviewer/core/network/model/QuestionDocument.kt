package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep

@NoArg
@Keep
data class QuestionDocument(
    val question: String,
    val correctChoices: List<String>,
    val wrongChoices: List<String>,
)
