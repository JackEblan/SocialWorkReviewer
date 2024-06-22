package com.android.socialworkreviewer.core.network.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FirestoreModule {

    @Singleton
    @Provides
    fun firestore(): FirebaseFirestore = Firebase.firestore
}