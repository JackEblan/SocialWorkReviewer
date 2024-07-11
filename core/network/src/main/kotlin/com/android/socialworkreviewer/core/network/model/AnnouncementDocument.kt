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

import androidx.annotation.Keep
import com.android.socialworkreviewer.core.model.Announcement
import com.google.firebase.Timestamp

@NoArg
@Keep
data class AnnouncementDocument(
    val id: String?,
    val date: Timestamp?,
    val title: String?,
    val message: String?,
) {
    companion object {
        const val DATE = "date"
    }
}

fun AnnouncementDocument.asExternalModel(): Announcement {
    return Announcement(
        id = id.toString(),
        title = title.toString(),
        message = message.toString(),
    )
}
