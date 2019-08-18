package io.ashdavies.databinding.network

import com.squareup.moshi.Json

internal data class Response<T>(
    @field:Json(name = "items") val items: List<T>,
    @field:Json(name = "nextPage") val nextPage: Int?,
    @field:Json(name = "total_count") val totalCount: Int
)
