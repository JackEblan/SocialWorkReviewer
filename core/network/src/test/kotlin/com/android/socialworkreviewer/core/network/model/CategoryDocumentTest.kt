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
package com.android.socialworkreviewer.core.network.model

import com.android.socialworkreviewer.core.model.QuestionSetting
import com.google.firebase.Timestamp
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class CategoryDocumentTest {

    @Test
    fun asExternalModel() {
        val category = CategoryDocument(
            id = "id",
            date = Timestamp(date = Date()),
            title = "title",
            description = "description",
            imageUrl = "imageUrl",
            questionSettings = listOf(
                QuestionSettingDocument(
                    numberOfQuestions = 10,
                    minutes = 10,
                ),
            ),
        ).asExternalModel()

        assertEquals(expected = "id", actual = category.id)
        assertEquals(expected = "title", actual = category.title)
        assertEquals(expected = "description", actual = category.description)
        assertEquals(expected = "imageUrl", actual = category.imageUrl)
        assertEquals(
            expected = listOf(
                QuestionSetting(
                    numberOfQuestions = 10,
                    minutes = 10,
                ),
            ),
            actual = category.questionSettings,
        )
    }
}
