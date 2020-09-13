package io.ashdavies.playground.network

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@Entity(primaryKeys = ["name"])
@JsonClass(generateAdapter = true)
internal data class Conference(
    @Json(name = "name") val name: String,
    @Json(name = "website") val website: String,
    @Json(name = "location") val location: String,
    @Json(name = "date_start") val dateStart: Date,
    @Json(name = "date_end") val dateEnd: Date,
    @Json(name = "cfp_start") val cfpStart: Date,
    @Json(name = "cfp_end") val cfpEnd: Date,
    @Json(name = "cfp_site") val cfpSite: String,
)
