package com.lucane.studio.flux.data.repository

import com.lucane.studio.flux.data.model.Symptom
import kotlinx.coroutines.flow.Flow

interface SymptomRepository {

    fun getAllSymptoms(): Flow<List<Symptom>>

    /** Returns the row id of the inserted/updated symptom. */
    suspend fun upsertSymptom(symptom: Symptom): Long
    suspend fun deleteSymptom(id: Long)
}