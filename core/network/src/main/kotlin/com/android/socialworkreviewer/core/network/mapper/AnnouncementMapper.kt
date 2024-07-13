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
package com.android.socialworkreviewer.core.network.mapper

import com.android.socialworkreviewer.core.model.Announcement
import com.android.socialworkreviewer.core.network.model.AnnouncementDocument

internal fun toAnnouncement(announcementDocument: AnnouncementDocument): Announcement {
    val id = announcementDocument.id.toString()

    val title = announcementDocument.title.toString()

    val message = announcementDocument.message.toString()

    return Announcement(
        id = id,
        title = title,
        message = message,
    )
}
