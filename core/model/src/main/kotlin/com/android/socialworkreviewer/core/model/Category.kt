package com.android.socialworkreviewer.core.model

data class Category(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val questionSettings: List<QuestionSetting>,
)