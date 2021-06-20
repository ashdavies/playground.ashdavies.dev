package io.ashdavies.playground.profile

import kotlinx.serialization.Serializable

@Serializable
internal data class RandomUser(
    val name: Name,
    val location: Location,
    val login: Login,
    val picture: Picture,
) {

    @Serializable
    data class Name(
        val first: String,
        val last: String,
    )

    @Serializable
    data class Location(
        val city: String,
        val country: String,
    )

    @Serializable
    data class Login(
        val uuid: String,
    )

    @Serializable
    data class Picture(
        val large: String,
    )
}