package com.android.socialworkreviewer.core.network.model

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Message

@NoArg
@Keep
data class MessageDocument(
    val title: String,
    val message: String,
)

fun MessageDocument.asExternalModel(): Message {
    return Message(title = title, message = message)
}
