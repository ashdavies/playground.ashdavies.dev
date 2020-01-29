package io.ashdavies.playground.github

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.ashdavies.playground.database.DateConverter
import io.ashdavies.playground.network.Conference

@Database(
    entities = [Conference::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(DateConverter::class)
internal abstract class ConferenceDatabase : RoomDatabase() {

  abstract fun dao(): ConferenceDao
}
