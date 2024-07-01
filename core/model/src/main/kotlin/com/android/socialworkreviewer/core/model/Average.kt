package com.android.socialworkreviewer.core.model

data class Average(
    val questionSettingIndex: Int,
    val score: Int,
    val numberOfQuestions: Int,
    val categoryId: String,
)
