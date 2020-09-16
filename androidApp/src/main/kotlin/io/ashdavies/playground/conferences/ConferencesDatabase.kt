package io.ashdavies.playground.conferences

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.ashdavies.playground.util.DateConverter
import io.ashdavies.playground.network.Conference

@Database(
    entities = [Conference::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(DateConverter::class)
internal abstract class ConferencesDatabase : RoomDatabase() {

    abstract fun dao(): ConferencesDao
}
