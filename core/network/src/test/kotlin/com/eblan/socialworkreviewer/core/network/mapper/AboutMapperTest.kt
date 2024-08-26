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
package com.eblan.socialworkreviewer.core.network.mapper

import com.eblan.socialworkreviewer.core.network.model.AboutDocument
import com.google.firebase.Timestamp
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class AboutMapperTest {

    @Test
    fun toAbout() {
        val links = listOf("link")

        val about = toAbout(
            aboutDocument = AboutDocument(
                id = "id",
                date = Timestamp(date = Date()),
                imageUrl = "imageUrl",
                title = "Title",
                name = "Name",
                message = "Message",
                links = links,
            ),
        )

        assertEquals(expected = "id", actual = about.id)
        assertEquals(expected = "imageUrl", actual = about.imageUrl)
        assertEquals(expected = "Title", actual = about.title)
        assertEquals(expected = "Name", actual = about.name)
        assertEquals(expected = "Message", actual = about.message)
        assertEquals(expected = links, actual = about.links)
    }
}
