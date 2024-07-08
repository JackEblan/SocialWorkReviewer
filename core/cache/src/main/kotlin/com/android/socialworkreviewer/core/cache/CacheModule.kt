package com.android.socialworkreviewer.core.cache

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface CacheModule {

    @Binds
    @Singleton
    fun inMemoryChoiceDataSource(impl: DefaultInMemoryChoiceDataSource): InMemoryChoiceDataSource
}