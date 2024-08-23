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
package com.eblan.socialworkreviewer.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal class Migration1To2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
           CREATE TABLE IF NOT EXISTS new_table (
               id INTEGER,
               questionSettingIndex INTEGER NOT NULL,
               numberOfQuestions INTEGER NOT NULL,
               score INTEGER NOT NULL,
               categoryId TEXT NOT NULL,
               PRIMARY KEY(id)
           )
            """.trimIndent(),
        )

        db.execSQL("INSERT INTO new_table (questionSettingIndex, numberOfQuestions, score, categoryId) SELECT * FROM AverageEntity")

        db.execSQL("DROP TABLE AverageEntity")

        db.execSQL("ALTER TABLE new_table RENAME TO AverageEntity")
    }
}
