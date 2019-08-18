package io.ashdavies.databinding.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "repos")
@JsonClass(generateAdapter = true)
internal data class Repo(
    @PrimaryKey @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "language") val language: String?,
    @field:Json(name = "updated_at") val updatedAt: String,
    @field:Json(name = "stargazers_count") val stargazersCount: String,
    @field:Json(name = "watchers_count") val watchersCount: String
)
