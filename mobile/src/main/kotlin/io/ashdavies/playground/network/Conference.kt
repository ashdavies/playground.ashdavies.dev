package io.ashdavies.playground.network

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import java.util.Date

@Keep
@JsonClass(generateAdapter = true)
internal data class Conference(
    val name: String,
    val country: String,
    val city: String,
    val region: String,
    val dateStart: Date,
    val dateEnd: Date,
    val website: String,
    val cfpStart: Date,
    val cfpEnd: Date,
    val cfpSite: String
)
