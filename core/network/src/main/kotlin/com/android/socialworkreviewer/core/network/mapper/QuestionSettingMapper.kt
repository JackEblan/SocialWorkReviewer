package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.model.QuestionSetting
import com.android.socialworkreviewer.core.network.model.QuestionSettingDocument

internal fun toQuestionSetting(questionSettingDocument: QuestionSettingDocument): QuestionSetting {
    val numberOfQuestions = questionSettingDocument.numberOfQuestions ?: 0

    val minutes = questionSettingDocument.minutes ?: 0

    return QuestionSetting(
        numberOfQuestions = numberOfQuestions,
        minutes = minutes,
    )
}