package io.ashdavies.playground.conferences

import io.ashdavies.playground.CoroutineTest
import io.ashdavies.playground.database.Conference
import io.ashdavies.playground.github.GitHubRepository
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConferencesFetcherTest : CoroutineTest() {

    @Test
    fun thingsShouldWork() = runBlockingTest {
        val service = GitHubService(DroidconBerlin())
        val fetcher = ConferencesFetcher(service)

        val conference: Conference = fetcher(Unit)
            .getOrThrow()
            .first()

        assertEquals(conference.name, "Droidcon")
    }
}

private data class DroidconBerlin(
    override val oid: String = "9361834b3e310ffd8992c1020eb868ebb56c564a",
    override val text: String = """
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
) : GitHubRepository.Entry