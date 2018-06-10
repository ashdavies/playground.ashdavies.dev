package io.ashdavies.databinding.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class Repo(
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "language") val language: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "stargazers_count") val stargazersCount: String,
    @Json(name = "watchers_count") val watchersCount: String
)
