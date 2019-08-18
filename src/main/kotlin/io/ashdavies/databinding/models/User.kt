package io.ashdavies.databinding.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class User(val name: String, val location: String)
