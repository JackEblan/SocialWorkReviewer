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
package com.android.socialworkreviewer.core.data.repository

import com.android.socialworkreviewer.core.model.Announcement
import com.android.socialworkreviewer.core.network.firestore.AnnouncementDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

internal class DefaultAnnouncementRepository @Inject constructor(private val announcementDataSource: AnnouncementDataSource) :
    AnnouncementRepository {
    override fun getAnnouncements(): Flow<List<Announcement>> {
        return announcementDataSource.getAnnouncementDocuments().distinctUntilChanged()
    }
}
