package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Category
import com.google.firebase.Timestamp

@NoArg
@Keep
data class CategoryDocument(
    val id: String?,
    val date: Timestamp?,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val questionSettings: List<QuestionSettingDocument>?,
) {
    companion object {
        const val DATE = "date"
    }
}

fun CategoryDocument.asExternalModel(): Category {
    return Category(
        id = id.toString(),
        title = title.toString(),
        description = description.toString(),
        imageUrl = imageUrl.toString(), average = 0.0,
        questionSettings = questionSettings?.map(QuestionSettingDocument::asExternalModel)
            ?: emptyList()
    )
}
