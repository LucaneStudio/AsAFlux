package com.lucane.studio.flux.data.mapper

import com.lucane.studio.flux.data.local.db.entity.SymptomEntity
import com.lucane.studio.flux.data.model.Symptom
import com.lucane.studio.flux.data.model.SymptomCategory

fun SymptomEntity.toDomain(): Symptom = Symptom(
    id       = id,
    name     = name,
    category = SymptomCategory.valueOf(category),
    isCustom = isCustom,
)

fun Symptom.toEntity(): SymptomEntity = SymptomEntity(
    id       = id,
    name     = name,
    category = category.name,
    isCustom = isCustom,
)