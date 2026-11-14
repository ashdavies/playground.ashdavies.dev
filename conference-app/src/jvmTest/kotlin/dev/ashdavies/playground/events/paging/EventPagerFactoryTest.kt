package dev.ashdavies.playground.events.paging

import androidx.paging.testing.asSnapshot
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.playground.event.EventFixtures
import dev.ashdavies.playground.invoke
import dev.ashdavies.playground.paging.EventPagerFactory
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.suspendLazyOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.time.Clock

internal class EventPagerFactoryTest {

    private val playgroundDatabase = PlaygroundDatabase(
        driver = JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
            schema = PlaygroundDatabase.Schema.synchronous(),
        ).apply(PlaygroundDatabase.Schema::create),
    )

    @Test
    @OptIn(ExperimentalMetroCoroutinesApi::class, ExperimentalSerializationApi::class)
    fun `should not include boundary events on same start date`() = runTest {
        val knownLocationDeque = ArrayDeque(Json.locations())
        val eventFixtures = EventFixtures.upcoming { it }
        val upcomingApiEventListSize = 24
        val pageSize = 12

        val upcomingApiEventList = List(Random.nextInt(upcomingApiEventListSize)) { it }
            .runningFold(LocalDate.nearFuture()) { acc, index ->
                when (index % pageSize) {
                    0 -> LocalDate.nearFuture(acc)
                    else -> acc
                }
            }
            .mapIndexed { index, startDate ->
                eventFixtures[index % (eventFixtures.size - 1)].let { fixture ->
                    fixture.copy(
                        value = fixture.value.copy(
                            location = knownLocationDeque.removeFirst().city,
                            dateStart = startDate,
                        ),
                    )
                }
            }

        val eventPager = EventPagerFactory(
            databaseFactory = suspendLazyOf(playgroundDatabase),
            eventsCallable = { Result.success(upcomingApiEventList) },
            coroutineContext = coroutineContext,
        ).create(PagerConfig(null, pageSize))

        val itemSnapshotList = eventPager.flow.asSnapshot {
            scrollTo(upcomingApiEventList.size)
        }

        assertEquals(upcomingApiEventList.size, itemSnapshotList.size)
    }
}

@ExperimentalSerializationApi
private fun Json.locations(): List<Location> = decodeFromStream(
    stream = Location::class.java
        .getResource("/locations.json")
        .let(::requireNotNull)
        .openStream(),
)

private fun LocalDate.Companion.nearFuture(
    startAt: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
        .date,
    random: Int = Random.nextInt(52),
) = startAt.plus(random, DateTimeUnit.WEEK)

@Serializable
internal data class Location(val city: String, val country: String)
