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

import com.eblan.socialworkreviewer.core.model.Category
import com.eblan.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.eblan.socialworkreviewer.core.network.mapper.toCategory
import com.eblan.socialworkreviewer.core.network.model.CategoryDocument
import com.eblan.socialworkreviewer.core.network.model.CategoryDocument.Companion.DATE
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class DefaultCategoryDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
) : CategoryDataSource {
    override fun getCategoryDocuments(): Flow<List<Category>> {
        return firestore.collection(CATEGORIES_COLLECTION).orderBy(DATE, Query.Direction.ASCENDING)
            .snapshots().mapNotNull { querySnapshots ->
                querySnapshots.mapNotNull { queryDocumentSnapshot ->
                    try {
                        toCategory(categoryDocument = queryDocumentSnapshot.toObject<CategoryDocument>())
                    } catch (e: RuntimeException) {
                        null
                    }
                }
            }.distinctUntilChanged()
    }

    override suspend fun getCategoryDocument(categoryDocumentId: String): Category? {
        val documentSnapshot =
            firestore.collection(CATEGORIES_COLLECTION).document(categoryDocumentId).get().await()

        return try {
            toCategory(categoryDocument = documentSnapshot.toObject<CategoryDocument>())
        } catch (e: RuntimeException) {
            null
        }
    }
}
