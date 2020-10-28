package io.ashdavies.playground.network

private const val ASG_CONFERENCES =
    "https://github.com/AndroidStudyGroup/conferences"

class ConferenceFactory(
    private val timeMillis: () -> Long,
) {

    operator fun invoke(): Conference = invoke(random())

    operator fun invoke(name: String) = Conference(
        name = name,
        website = ASG_CONFERENCES,
        location = "Online",
        dateStart = timeMillis(),
        dateEnd = timeMillis(),
        cfpStart = timeMillis(),
        cfpEnd = timeMillis(),
        cfpSite = ASG_CONFERENCES,
    )

    private fun random(): String = listOf(
        "Droidcon NYC",
        "DevFest Berlin",
        "Kotlin Everywhere Coimbra",
        "KotlinKonf '19",
    ).random()
}
