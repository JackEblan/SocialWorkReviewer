package com.android.socialworkreviewer.core.network.firestore

import com.android.socialworkreviewer.core.model.Question
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultQuestionDataSource @Inject constructor(private val firestore: FirebaseFirestore) :
    QuestionDataSource {
    override fun getQuestions(category: String): Flow<List<Question>> {
        return firestore.collection(category).snapshots().map { it.toObjects<Question>() }
    }
}