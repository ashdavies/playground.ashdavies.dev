package dev.ashdavies.playground.events.paging

import androidx.paging.testing.asSnapshot
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.playground.paging.EventPagerFactory
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
import kotlin.time.ExperimentalTime
import kotlin.uuid.Uuid
import dev.ashdavies.http.common.models.ApiConference as ApiEvent

internal class EventPagerFactoryTest {

    private val playgroundDatabase = PlaygroundDatabase(
        driver = JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
            schema = PlaygroundDatabase.Schema.synchronous(),
        ).apply(PlaygroundDatabase.Schema::create),
    )

    @Test
    fun `should not include boundary events on same start date`() = runTest {
        val knownLocationDeque = ArrayDeque(Json.locations())
        val upcomingApiEventListSize = 24
        val pageSize = 12

        val upcomingApiEventList = List(Random.nextInt(upcomingApiEventListSize)) { it }
            .runningFold(LocalDate.nearFuture()) { acc, index ->
                when (index % pageSize) {
                    0 -> LocalDate.nearFuture(acc)
                    else -> acc
                }
            }
            .map { startDate ->
                apiEvent(
                    location = knownLocationDeque.removeFirst().city,
                    dateStart = startDate,
                )
            }

        val eventPager = EventPagerFactory(
            eventsCallable = { Result.success(upcomingApiEventList) },
            eventsQueries = { playgroundDatabase.eventQueries },
            coroutineContext = coroutineContext,
        ).create(PagerConfig(null, pageSize))

        val itemSnapshotList = eventPager.flow.asSnapshot {
            scrollTo(upcomingApiEventList.size)
        }

        assertEquals(upcomingApiEventList.size, itemSnapshotList.size)
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal fun Json.locations(): List<Location> = decodeFromStream(
    stream = Location::class.java
        .getResource("/locations.json")
        .let(::requireNotNull)
        .openStream(),
)

@OptIn(ExperimentalTime::class)
private fun LocalDate.Companion.nearFuture(
    startAt: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
        .date,
    random: Int = Random.nextInt(52),
) = startAt.plus(random, DateTimeUnit.WEEK)

private fun apiEvent(location: String, dateStart: LocalDate) = ApiEvent(
    id = "${Uuid.random()}",
    name = "Playground Conf $location '${"${dateStart.year}".substring(2)}",
    website = "https://playground.ashdavies.dev/",
    location = location,
    imageUrl = null,
    status = null,
    online = false,
    dateStart = "$dateStart",
    dateEnd = "${dateStart.plus(Random.nextInt(3), DateTimeUnit.DAY)}",
    cfp = null,
)

@Serializable
internal data class Location(val city: String, val country: String)
