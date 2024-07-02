package com.android.socialworkreviewer.core.model

data class Category(
    val id: String,
    val orderNumber: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val average: Double = 0.0,
    val questionSettings: List<QuestionSetting>,
)