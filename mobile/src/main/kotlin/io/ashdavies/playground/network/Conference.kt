package io.ashdavies.playground.network

import androidx.room.Entity
import java.util.Date

@Entity(tableName = "conferences")
internal data class Conference(
    val uuid: String,
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
