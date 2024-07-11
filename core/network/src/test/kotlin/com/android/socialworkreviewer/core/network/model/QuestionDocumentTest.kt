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

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class QuestionDocumentTest {

    @Test
    fun asExternalModel() {
        val question = QuestionDocument(
            question = "question",
            correctChoices = listOf("A"),
            wrongChoices = listOf("B"),
        ).asExternalModel()

        assertEquals(expected = "question", actual = question.question)
        assertEquals(expected = listOf("A"), actual = question.correctChoices)
        assertEquals(expected = listOf("B"), actual = question.wrongChoices)
        assertEquals(expected = listOf("B"), actual = question.wrongChoices)
        assertTrue(question.choices.containsAll(listOf("A") + listOf("B")))
    }
}
