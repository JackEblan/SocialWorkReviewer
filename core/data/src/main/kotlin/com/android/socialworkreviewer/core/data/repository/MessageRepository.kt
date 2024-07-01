package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Message

interface MessageRepository {
    suspend fun getMessage(): Message?
}