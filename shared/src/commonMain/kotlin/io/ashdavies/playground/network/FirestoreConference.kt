package io.ashdavies.playground.network

import io.ashdavies.playground.network.FirestoreField.BooleanField
import io.ashdavies.playground.network.FirestoreField.DateField
import io.ashdavies.playground.network.FirestoreField.StringField
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FirestoreConference(

    @SerialName("name")
    val name: StringField,

    @SerialName("website")
    val website: StringField,

    @SerialName("location")
    val location: StringField,

    @SerialName("status")
    val status: StringField? = null,

    @SerialName("dateStart")
    val dateStart: DateField,

    @SerialName("dateEnd")
    val dateEnd: DateField,

    @SerialName("cfpStart")
    val cfpStart: DateField? = null,

    @SerialName("cfpEnd")
    val cfpEnd: DateField? = null,

    @SerialName("cfpSite")
    val cfpSite: StringField? = null,

    @SerialName("online")
    val online: BooleanField? = null,
)
