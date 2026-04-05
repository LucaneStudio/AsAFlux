package com.lucane.studio.flux.data.local.db.converter

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Type converter for [LocalDate] columns.
 *
 * Current entities already store dates as ISO-8601 strings directly,
 * so this converter is not active in Phase 1. It is registered in
 * [@TypeConverters] now to be ready for future columns that may need it
 * (e.g. contraception start date in settings).
 */
class DateConverter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }
}