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
package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.common.Dispatcher
import com.android.socialworkreviewer.core.common.SwrDispatchers.IO
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.android.socialworkreviewer.core.network.model.CategoryDocument
import com.android.socialworkreviewer.core.network.model.CategoryDocument.Companion.DATE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultCategoryDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : CategoryDataSource {
    override fun getCategoryDocuments(): Flow<List<CategoryDocument>> {
        return firestore.collection(CATEGORIES_COLLECTION).orderBy(DATE, Query.Direction.ASCENDING)
            .snapshots()
            .mapNotNull { querySnapshots ->
                querySnapshots.mapNotNull { queryDocumentSnapshot ->
                    try {
                        queryDocumentSnapshot.toObject()
                    } catch (e: RuntimeException) {
                        null
                    }
                }
            }
    }

    override suspend fun getCategoryDocument(categoryDocumentId: String): CategoryDocument? {
        return withContext(ioDispatcher) {
            val documentSnapshot =
                firestore.collection(CATEGORIES_COLLECTION).document(categoryDocumentId).get()
                    .await()

            try {
                documentSnapshot.toObject<CategoryDocument>()
            } catch (e: RuntimeException) {
                null
            }
        }
    }
}
