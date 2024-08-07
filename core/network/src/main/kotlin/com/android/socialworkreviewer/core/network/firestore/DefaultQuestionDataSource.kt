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
import com.android.socialworkreviewer.core.common.SwrDispatchers
import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.android.socialworkreviewer.core.network.firestore.QuestionDataSource.Companion.QUESTIONS_COLLECTION
import com.android.socialworkreviewer.core.network.mapper.toQuestion
import com.android.socialworkreviewer.core.network.model.QuestionDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultQuestionDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    @Dispatcher(SwrDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : QuestionDataSource {
    override suspend fun getQuestions(id: String): List<Question> {
        return withContext(ioDispatcher) {
            firestore.collection(CATEGORIES_COLLECTION).document(id)
                .collection(QUESTIONS_COLLECTION).get().await()
                .mapNotNull { queryDocumentSnapshot ->
                    try {
                        toQuestion(questionDocument = queryDocumentSnapshot.toObject<QuestionDocument>())
                    } catch (e: RuntimeException) {
                        null
                    }
                }
        }
    }
}
