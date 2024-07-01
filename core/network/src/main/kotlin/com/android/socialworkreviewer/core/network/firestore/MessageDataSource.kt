package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.network.model.MessageDocument

interface MessageDataSource {
    suspend fun getMessage(): MessageDocument?

    companion object {
        const val MESSAGES_COLLECTION = "messages"

        const val MESSAGES_DOCUMENT = "message"
    }
}