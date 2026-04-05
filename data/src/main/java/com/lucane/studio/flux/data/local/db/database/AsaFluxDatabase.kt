package com.lucane.studio.flux.data.local.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lucane.studio.flux.data.local.db.converter.DateConverter
import com.lucane.studio.flux.data.local.db.dao.DailyLogDao
import com.lucane.studio.flux.data.local.db.dao.SymptomDao
import com.lucane.studio.flux.data.local.db.entity.DailyLogEntity
import com.lucane.studio.flux.data.local.db.entity.DailyLogSymptomCrossRef
import com.lucane.studio.flux.data.local.db.entity.SymptomEntity

/**
 * Main Room database for AsAFlux.
 *
 * exportSchema is set to true: Room generates a versioned JSON schema file
 * under /schemas on every version bump. Commit these files — they serve
 * as migration documentation and are validated by Room at build time.
 */
@Database(
    entities = [
        DailyLogEntity::class,
        SymptomEntity::class,
        DailyLogSymptomCrossRef::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(DateConverter::class)
abstract class AsaFluxDatabase : RoomDatabase() {
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun symptomDao(): SymptomDao
}