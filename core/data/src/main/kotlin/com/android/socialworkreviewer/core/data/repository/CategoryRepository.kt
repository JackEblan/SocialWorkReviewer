package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Category
import com.android.socialworkreviewer.core.model.QuestionSetting
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>

    suspend fun getQuestionSettingsByCategory(id: String): List<QuestionSetting>
}