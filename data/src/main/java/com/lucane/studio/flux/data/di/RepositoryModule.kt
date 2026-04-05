package com.lucane.studio.flux.data.di

import com.lucane.studio.flux.data.repository.DailyLogRepository
import com.lucane.studio.flux.data.repository.DailyLogRepositoryImpl
import com.lucane.studio.flux.data.repository.SymptomRepository
import com.lucane.studio.flux.data.repository.SymptomRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDailyLogRepository(
        impl: DailyLogRepositoryImpl,
    ): DailyLogRepository

    @Binds
    @Singleton
    abstract fun bindSymptomRepository(
        impl: SymptomRepositoryImpl,
    ): SymptomRepository
}