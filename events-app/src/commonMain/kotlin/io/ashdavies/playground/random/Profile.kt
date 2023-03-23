package io.ashdavies.playground.random

import io.ashdavies.playground.Profile
import io.ashdavies.playground.random.RandomUser

internal fun Profile(value: RandomUser) = Profile(
    name = "${value.name.first} ${value.name.last}",
    location = "${value.location.city}, ${value.location.country}",
    email = value.email,
    uuid = value.login.uuid,
    username = value.login.username,
    registered = value.registered.date,
    picture = value.picture.large,
)
