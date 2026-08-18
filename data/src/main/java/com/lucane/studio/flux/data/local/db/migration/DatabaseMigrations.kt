package com.lucane.studio.flux.data.local.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds `isPeriod`, decoupling "this day belongs to a declared bleeding
 * period" from `flowIntensity` — a day can carry a declared flow intensity
 * (e.g. spotting) without being part of a period. Existing rows are
 * backfilled so pre-migration data keeps its prior meaning (any non-NONE
 * flow was, until now, the only signal a period day had).
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE daily_logs ADD COLUMN isPeriod INTEGER NOT NULL DEFAULT 0")
        db.execSQL("UPDATE daily_logs SET isPeriod = 1 WHERE flowIntensity != 'NONE'")
    }
}
