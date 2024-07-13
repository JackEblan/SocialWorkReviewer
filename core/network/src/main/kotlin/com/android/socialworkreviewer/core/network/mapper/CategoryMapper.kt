package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.model.Category
import com.android.socialworkreviewer.core.network.model.CategoryDocument

internal fun toCategory(categoryDocument: CategoryDocument?): Category {
    val id = categoryDocument?.id.toString()

    val title = categoryDocument?.title.toString()

    val description = categoryDocument?.description.toString()

    val imageUrl = categoryDocument?.imageUrl.toString()

    val average = 0.0

    val questionSettings = categoryDocument?.questionSettings?.map { questionSettingDocument ->
        toQuestionSetting(questionSettingDocument = questionSettingDocument)
    } ?: emptyList()

    return Category(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        average = average,
        questionSettings = questionSettings,
    )
}
