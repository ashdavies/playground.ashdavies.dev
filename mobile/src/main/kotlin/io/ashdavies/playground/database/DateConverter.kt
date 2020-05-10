package io.ashdavies.playground.database

import androidx.room.TypeConverter
import java.util.Date

internal class DateConverter {

    @TypeConverter
    fun Long.toDate(): Date = Date(this)

    @TypeConverter
    fun Date.toLong(): Long = time
}
