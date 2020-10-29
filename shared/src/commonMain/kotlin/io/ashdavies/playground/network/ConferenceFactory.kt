@file:Suppress("FunctionName")

package io.ashdavies.playground.network

private const val ASG_CONFERENCES =
    "https://github.com/AndroidStudyGroup/conferences"

private val randomName: String
    get() = listOf(
        "Droidcon NYC",
        "DevFest Berlin",
        "Kotlin Everywhere Coimbra",
        "KotlinKonf '19",
    ).random()

private class TimeConferenceFactory(
    private val timeInMillis: () -> Long,
) : ConferenceFactory {

    override fun invoke(name: String) = Conference(
        name = name,
        website = ASG_CONFERENCES,
        location = "Online",
        dateStart = timeInMillis(),
        dateEnd = timeInMillis(),
        cfpStart = timeInMillis(),
        cfpEnd = timeInMillis(),
        cfpSite = ASG_CONFERENCES,
    )
}

interface ConferenceFactory {

    operator fun invoke(name: String): Conference
}

fun ConferenceFactory(timeInMillis: () -> Long): ConferenceFactory =
    TimeConferenceFactory(timeInMillis)

operator fun ConferenceFactory.invoke(): Conference =
    invoke(name = randomName)
