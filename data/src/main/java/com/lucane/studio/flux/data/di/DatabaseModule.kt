package com.lucane.studio.flux.data.di

import android.content.Context
import androidx.room.Room
import com.lucane.studio.flux.data.local.db.database.AsaFluxDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AsaFluxDatabase =
        Room.databaseBuilder(
            context,
            AsaFluxDatabase::class.java,
            "asaflux.db",
        ).build()

    @Provides
    fun provideDailyLogDao(database: AsaFluxDatabase) = database.dailyLogDao()

    @Provides
    fun provideSymptomDao(database: AsaFluxDatabase) = database.symptomDao()
}