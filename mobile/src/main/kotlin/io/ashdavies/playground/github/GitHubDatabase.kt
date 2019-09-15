package io.ashdavies.playground.github

import androidx.room.Database
import androidx.room.RoomDatabase
import io.ashdavies.playground.models.Repo

@Database(
    entities = [Repo::class],
    exportSchema = false,
    version = 1
)
internal abstract class GitHubDatabase : RoomDatabase() {

  abstract fun repo(): GitHubDao
}
