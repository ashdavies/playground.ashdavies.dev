package io.ashdavies.playground.conferences

import io.ashdavies.playground.yaml.Yaml
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConferencesFetcherTest {

    @Test
    fun thingsShouldWork() {
        val conference = Yaml
            .decodeFromString(ConferenceYaml.serializer(), DroidconBerlin)
            .toConference("1234")

        print(conference)
        assertEquals(conference.name, "Droidcon")
    }

    @Test
    @Ignore
    fun thingsShouldBreak() {
        assertEquals(listOf(1, 2, 3).reversed(), listOf(1, 2, 3))
    }
}

private val DroidconBerlin = """
---
name: "Droidcon"
website: https://berlin.droidcon.com/
location: Berlin, Germany

date_start: 2021-10-20
date_end:   2021-10-22

cfp:
  start:  2020-01-06
  end:    2021-08-31
  site:   https://sessionize.com/droidcon-berlin-2021/
---
""".trimIndent()