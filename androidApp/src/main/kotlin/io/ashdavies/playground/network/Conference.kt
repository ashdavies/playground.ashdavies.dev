@file:UseSerializers(DateSerializer::class)

package io.ashdavies.playground.network

import androidx.room.Entity
import io.ashdavies.playground.util.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Date

@Serializable
@Entity(primaryKeys = ["name"])
internal data class Conference(
    @SerialName("name") val name: String,
    @SerialName("website") val website: String,
    @SerialName("location") val location: String,
    @SerialName("date_start") val dateStart: Date,
    @SerialName("date_end") val dateEnd: Date,
    @SerialName("cfp_start") val cfpStart: Date,
    @SerialName("cfp_end") val cfpEnd: Date,
    @SerialName("cfp_site") val cfpSite: String,
)
