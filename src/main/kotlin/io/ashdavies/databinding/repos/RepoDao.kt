package io.ashdavies.databinding.repos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.ashdavies.databinding.models.Repo

@Dao
internal interface RepoDao {

  @Insert(onConflict = REPLACE)
  fun insert(posts: List<Repo>)

  @Query("SELECT * FROM repos WHERE name == :user ORDER BY stargazersCount DESC")
  fun repos(user: CharSequence): LiveData<List<Repo>>
}
