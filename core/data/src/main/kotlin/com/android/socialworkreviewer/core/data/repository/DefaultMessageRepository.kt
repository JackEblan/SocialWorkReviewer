package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Message
import com.android.socialworkreviewer.core.network.firestore.MessageDataSource
import com.android.socialworkreviewer.core.network.model.asExternalModel
import javax.inject.Inject

internal class DefaultMessageRepository @Inject constructor(private val messageDataSource: MessageDataSource) :
    MessageRepository {
    override suspend fun getMessage(): Message? {
        return messageDataSource.getMessage()?.asExternalModel()
    }
}