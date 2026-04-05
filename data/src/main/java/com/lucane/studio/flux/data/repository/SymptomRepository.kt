package com.lucane.studio.flux.data.repository

import com.lucane.studio.flux.data.model.Symptom
import kotlinx.coroutines.flow.Flow

interface SymptomRepository {

    fun getAllSymptoms(): Flow<List<Symptom>>
    suspend fun upsertSymptom(symptom: Symptom)
    suspend fun deleteSymptom(id: Long)
}