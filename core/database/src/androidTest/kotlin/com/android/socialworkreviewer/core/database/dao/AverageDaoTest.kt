package com.android.socialworkreviewer.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.android.socialworkreviewer.core.database.AppDatabase
import com.android.socialworkreviewer.core.database.model.AverageEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class AverageDaoTest {
    private lateinit var averageDao: AverageDao

    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
        ).build()

        averageDao = db.averageDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAverageEntitiesByCategory() = runTest {
        val averageEntity = AverageEntity(
            questionSettingIndex = 0, score = 10, numberOfQuestions = 10, categoryId = "categoryId"
        )

        averageDao.upsertAverageEntity(averageEntity)

        assertTrue {
            averageDao.getAverageEntitiesByCategory("categoryId").first().isNotEmpty()
        }
    }

}