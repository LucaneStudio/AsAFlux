package com.lucane.studio.flux.data.local.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.lucane.studio.flux.data.local.db.entity.DailyLogEntity
import com.lucane.studio.flux.data.local.db.entity.DailyLogSymptomCrossRef
import com.lucane.studio.flux.data.local.db.entity.SymptomEntity

/** Room multimap result combining a [DailyLogEntity] with its associated symptoms. */
data class DailyLogWithSymptoms(
    @Embedded val dailyLog: DailyLogEntity,
    @Relation(
        parentColumn = "date",
        entityColumn = "id",
        associateBy = Junction(
            value = DailyLogSymptomCrossRef::class,
            parentColumn = "date",
            entityColumn = "symptomId",
        ),
    )
    val symptoms: List<SymptomEntity>,
)