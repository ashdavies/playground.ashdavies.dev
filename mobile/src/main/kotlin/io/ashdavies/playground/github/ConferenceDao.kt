package io.ashdavies.playground.github

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.ashdavies.playground.network.Conference

@Dao
internal interface ConferenceDao {

  @Insert(onConflict = REPLACE)
  suspend fun insert(conferences: List<Conference>)

  @Query("SELECT * FROM conference ORDER BY dateStart ASC")
  fun conferences(): DataSource.Factory<Int, Conference>
}
