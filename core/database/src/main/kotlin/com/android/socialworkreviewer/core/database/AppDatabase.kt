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
package com.android.socialworkreviewer.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.socialworkreviewer.core.database.dao.AverageDao
import com.android.socialworkreviewer.core.database.model.AverageEntity

@Database(
    entities = [AverageEntity::class],
    version = 1,
    exportSchema = true,
)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun averageDao(): AverageDao

    companion object {
        const val DATABASE_NAME = "Swr.db"
    }
}
