package com.lucane.studio.flux.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lucane.studio.flux.data.local.db.entity.SymptomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomDao {

    @Query("SELECT * FROM symptoms ORDER BY category ASC, name ASC")
    fun getAllSymptoms(): Flow<List<SymptomEntity>>

    @Upsert
    suspend fun upsertSymptom(entity: SymptomEntity)

    @Query("DELETE FROM symptoms WHERE id = :id")
    suspend fun deleteSymptom(id: Long)
}