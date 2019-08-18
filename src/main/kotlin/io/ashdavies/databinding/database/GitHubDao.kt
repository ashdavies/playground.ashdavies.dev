package io.ashdavies.databinding.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.ashdavies.databinding.models.Repo

@Dao
internal interface GitHubDao {

  @Insert(onConflict = REPLACE)
  suspend fun insert(posts: List<Repo>)

  @Query("SELECT * FROM repos WHERE name == :user ORDER BY stargazersCount DESC")
  suspend fun repos(user: String): List<Repo>
}
