package io.ashdavies.playground.github

import androidx.room.Database
import androidx.room.RoomDatabase
import io.ashdavies.playground.network.Conference

@Database(
    entities = [Conference::class],
    exportSchema = false,
    version = 1
)
internal abstract class ConferenceDatabase : RoomDatabase() {

  abstract fun dao(): ConferenceDao
}
