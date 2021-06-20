package io.ashdavies.playground.profile

import kotlinx.serialization.Serializable

@Serializable
internal data class RandomUser(
    val name: Name,
    val city: String,
    val country: String,
    val login: Login,
    val picture: Picture,
) {

    @Serializable
    data class Name(
        val first: String,
        val last: String,
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