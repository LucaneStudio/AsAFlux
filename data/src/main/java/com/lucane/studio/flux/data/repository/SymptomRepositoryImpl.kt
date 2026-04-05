package com.lucane.studio.flux.data.repository

import com.lucane.studio.flux.data.local.db.dao.SymptomDao
import com.lucane.studio.flux.data.mapper.toDomain
import com.lucane.studio.flux.data.mapper.toEntity
import com.lucane.studio.flux.data.model.Symptom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SymptomRepositoryImpl @Inject constructor(
    private val dao: SymptomDao,
) : SymptomRepository {

    override fun getAllSymptoms(): Flow<List<Symptom>> =
        dao.getAllSymptoms().map { list -> list.map { it.toDomain() } }

    override suspend fun upsertSymptom(symptom: Symptom) =
        dao.upsertSymptom(symptom.toEntity())

    override suspend fun deleteSymptom(id: Long) =
        dao.deleteSymptom(id)
}