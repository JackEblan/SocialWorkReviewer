package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.QuestionSetting

@NoArg
@Keep
data class QuestionSettingDocument(
    val numberOfQuestions: Int, val minutes: Int
)

fun QuestionSettingDocument.asExternalModel(): QuestionSetting {
    return QuestionSetting(numberOfQuestions = numberOfQuestions, minutes = minutes)
}
