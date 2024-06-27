package com.android.socialworkreviewer.core.network.model

import com.android.socialworkreviewer.core.model.QuestionSetting

@NoArg
data class QuestionSettingDocument(
    val numberOfQuestions: Int, val minutes: Int
)

fun QuestionSettingDocument.asExternalModel(): QuestionSetting {
    return QuestionSetting(numberOfQuestions = numberOfQuestions, minutes = minutes)
}
