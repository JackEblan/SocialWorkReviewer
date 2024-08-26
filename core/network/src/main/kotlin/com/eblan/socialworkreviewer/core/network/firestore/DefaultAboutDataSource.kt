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
package com.eblan.socialworkreviewer.core.network.firestore

import com.eblan.socialworkreviewer.core.model.About
import com.eblan.socialworkreviewer.core.network.firestore.AboutDataSource.Companion.ABOUTS_COLLECTION
import com.eblan.socialworkreviewer.core.network.mapper.toAbout
import com.eblan.socialworkreviewer.core.network.model.AboutDocument
import com.eblan.socialworkreviewer.core.network.model.AnnouncementDocument.Companion.DATE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class DefaultAboutDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) : AboutDataSource {
    override fun getAboutDocuments(): Flow<List<About>> {
        return firestore.collection(ABOUTS_COLLECTION).orderBy(DATE, Query.Direction.DESCENDING)
            .snapshots().mapNotNull { querySnapshots ->
                querySnapshots.mapNotNull { queryDocumentSnapshot ->
                    try {
                        toAbout(aboutDocument = queryDocumentSnapshot.toObject<AboutDocument>())
                    } catch (e: RuntimeException) {
                        null
                    }
                }
            }.distinctUntilChanged()
    }
}
