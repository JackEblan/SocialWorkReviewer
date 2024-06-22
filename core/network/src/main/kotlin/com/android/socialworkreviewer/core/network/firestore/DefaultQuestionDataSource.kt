package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.model.Question
import com.android.socialworkreviewer.core.network.firestore.CategoryDataSource.Companion.CATEGORIES_COLLECTION
import com.android.socialworkreviewer.core.network.firestore.QuestionDataSource.Companion.QUESTIONS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class DefaultQuestionDataSource @Inject constructor(private val firestore: FirebaseFirestore) :
    QuestionDataSource {
    override fun getQuestions(id: String): Flow<List<Question>> {
        return firestore.collection(CATEGORIES_COLLECTION).document(id)
            .collection(QUESTIONS_COLLECTION).snapshots().mapNotNull { it.toObjects<Question>() }
    }
}