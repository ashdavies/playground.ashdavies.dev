package io.ashdavies.databinding.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.ashdavies.databinding.models.Repo

@Dao
internal interface GitHubDao {

  @Insert(onConflict = REPLACE)
  suspend fun insert(repos: List<Repo>)

  @Query("SELECT * FROM repos WHERE fullName LIKE :query ORDER BY stargazersCount DESC, fullName ASC")
  suspend fun repos(query: String): List<Repo>
}
