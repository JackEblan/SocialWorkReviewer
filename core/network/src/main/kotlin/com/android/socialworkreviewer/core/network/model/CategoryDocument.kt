package com.android.socialworkreviewer.core.network.model

import com.android.socialworkreviewer.core.model.Category

@NoArg
data class CategoryDocument(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val questionSettings: List<QuestionSettingDocument>,
)

fun CategoryDocument.asExternalModel(): Category {
    return Category(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        questionSettings = questionSettings.map(QuestionSettingDocument::asExternalModel)
    )
}
