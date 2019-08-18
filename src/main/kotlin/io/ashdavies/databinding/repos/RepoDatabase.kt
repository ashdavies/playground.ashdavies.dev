package io.ashdavies.databinding.repos

import androidx.room.Database
import androidx.room.RoomDatabase
import io.ashdavies.databinding.models.Repo

@Database(
    entities = [Repo::class],
    exportSchema = false,
    version = 1
)
internal abstract class RepoDatabase : RoomDatabase() {

  abstract fun dao(): RepoDao
}
