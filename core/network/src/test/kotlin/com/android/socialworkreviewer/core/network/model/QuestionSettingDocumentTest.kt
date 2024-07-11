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

import org.junit.Test
import kotlin.test.assertEquals

class QuestionSettingDocumentTest {

    @Test
    fun asExternalModel() {
        val questionSetting =
            QuestionSettingDocument(numberOfQuestions = 10, minutes = 10).asExternalModel()

        assertEquals(expected = 10, actual = questionSetting.numberOfQuestions)
        assertEquals(expected = 10, actual = questionSetting.minutes)
    }
}
