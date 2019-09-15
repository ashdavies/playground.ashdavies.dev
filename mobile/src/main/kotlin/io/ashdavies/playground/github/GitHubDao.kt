package io.ashdavies.playground.github

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.ashdavies.playground.models.Repo

@Dao
internal interface GitHubDao {

  @Insert(onConflict = REPLACE)
  suspend fun insert(repos: List<Repo>)

  @Query("SELECT * FROM repos WHERE name LIKE :query ORDER BY stargazersCount DESC, name ASC")
  fun repos(query: String): DataSource.Factory<Int, Repo>
}
