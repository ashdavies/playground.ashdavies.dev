package io.ashdavies.playground.network

import io.ashdavies.playground.database.Conference
import kotlinx.serialization.Serializable

@Serializable
internal data class ConferenceJson(
    val id: String,
    val name: String,
    val website: String,
    val location: String,
    val status: String?,
    val online: Boolean?,
    val dateStart: String,
    val dateEnd: String,
    val cfpStart: String?,
    val cfpEnd: String?,
    val cfpSite: String?,
)

internal fun ConferenceJson.toConference() = Conference(
    id = id,
    name = name,
    website = website,
    location = location,
    status = status,
    online = online,
    dateStart = dateStart,
    dateEnd = dateEnd,
    cfpStart = cfpStart,
    cfpEnd = cfpEnd,
    cfpSite = cfpSite,
)