package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Category

@NoArg
@Keep
data class CategoryDocument(
    val id: String?,
    val orderNumber: Int?,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val questionSettings: List<QuestionSettingDocument>?,
)

fun CategoryDocument.asExternalModel(): Category {
    return Category(
        id = id.toString(), orderNumber = orderNumber ?: 0,
        title = title.toString(),
        description = description.toString(),
        imageUrl = imageUrl.toString(),
        questionSettings = questionSettings?.map(QuestionSettingDocument::asExternalModel)
            ?: emptyList()
    )
}
