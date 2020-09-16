package io.ashdavies.playground.conferences

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.ashdavies.playground.network.Conference

@Dao
internal interface ConferencesDao {

    @Query("SELECT * FROM conference")
    suspend fun getAll(): List<Conference>

    @Query("SELECT * FROM conference WHERE name = :name")
    suspend fun get(name: String): Conference

    @Insert(onConflict = REPLACE)
    suspend fun insert(conferences: List<Conference>)

    @Delete
    suspend fun delete(conference: Conference)

    @Query("DELETE FROM conference")
    suspend fun deleteAll()
}
