package krtonga.github.io.rovertrack.core.db.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

// public static class (singleton that can't be instantiated)
object DateConverter {

    @JvmStatic // Yes Room, this is a static method...
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)

    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}