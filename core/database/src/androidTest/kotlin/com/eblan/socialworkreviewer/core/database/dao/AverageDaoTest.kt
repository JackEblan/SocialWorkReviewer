/*
 *
 *   Copyright 2023 Einstein Blanco
 *
 *   Licensed under the GNU General Public License v3.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.eblan.socialworkreviewer.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.eblan.socialworkreviewer.core.database.AppDatabase
import com.eblan.socialworkreviewer.core.database.model.AverageEntity
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
            questionSettingIndex = 0,
            score = 10,
            numberOfQuestions = 10,
            categoryId = "categoryId",
        )

        averageDao.upsertAverageEntity(averageEntity)

        assertTrue {
            averageDao.getAverageEntitiesByCategory("categoryId").first().isNotEmpty()
        }
    }
}
