//@file:UseSerializers(DateSerializer::class)

package io.ashdavies.playground.network

import java.util.Date
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable

//@Serializable
//@Entity(primaryKeys = ["name"])
internal data class Conference(
    /*@SerialName("name")*/ val name: String,
    /*@SerialName("website")*/ val website: String,
    /*@SerialName("location")*/ val location: String,
    /*@SerialName("date_start")*/ val dateStart: Date,
    /*@SerialName("date_end")*/ val dateEnd: Date,
    /*@SerialName("cfp_start")*/ val cfpStart: Date,
    /*@SerialName("cfp_end")*/ val cfpEnd: Date,
    /*@SerialName("cfp_site")*/ val cfpSite: String,
)
