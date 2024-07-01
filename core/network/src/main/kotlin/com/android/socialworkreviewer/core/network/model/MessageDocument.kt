package com.android.socialworkreviewer.core.network.model

import com.android.socialworkreviewer.core.model.Message

data class MessageDocument(
    val title: String,
    val message: String,
)

fun MessageDocument.asExternalModel(): Message {
    return Message(title = title, message = message)
}
