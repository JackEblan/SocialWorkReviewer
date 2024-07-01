package com.android.socialworkreviewer.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.socialworkreviewer.core.model.Average

@Entity
data class AverageEntity(
    @PrimaryKey val questionSettingIndex: Int,
    val score: Int,
    val numberOfQuestions: Int,
    val categoryId: String,
)

fun AverageEntity.asExternalModel(): Average {
    return Average(
        questionSettingIndex = questionSettingIndex,
        score = score,
        numberOfQuestions = numberOfQuestions,
        categoryId = categoryId
    )
}

fun Average.asEntity(): AverageEntity {
    return AverageEntity(
        questionSettingIndex = questionSettingIndex,
        score = score,
        numberOfQuestions = numberOfQuestions,
        categoryId = categoryId
    )
}