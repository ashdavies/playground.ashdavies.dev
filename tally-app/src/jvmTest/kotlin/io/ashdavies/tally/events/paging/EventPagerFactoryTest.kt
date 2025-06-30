package io.ashdavies.tally.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.testing.asSnapshot
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.ashdavies.tally.PlaygroundDatabase
import io.ashdavies.tally.tooling.UnitTestResources
import io.ashdavies.tally.tooling.locations
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import io.ashdavies.http.common.models.Event as ApiEvent

@OptIn(ExperimentalPagingApi::class)
internal class EventPagerFactoryTest {

    private val playgroundDatabase = PlaygroundDatabase(
        driver = JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
            schema = PlaygroundDatabase.Schema.synchronous(),
        ).apply(PlaygroundDatabase.Schema::create),
    )

    @Test
    fun `should not include boundary events on same start date`() = runTest(timeout = 1.seconds) {
        val knownLocationDeque = ArrayDeque(UnitTestResources.locations())
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
                tallyConf(
                    location = knownLocationDeque.removeFirst().city,
                    dateStart = startDate,
                )
            }

        val eventPager = eventPager(
            eventsCallable = { upcomingApiEventList },
            eventsQueries = playgroundDatabase.eventsQueries,
            pageSize = pageSize,
            context = coroutineContext,
        )

        val itemSnapshotList = eventPager.flow.asSnapshot {
            scrollTo(upcomingApiEventList.size)
        }

        assertEquals(upcomingApiEventList.size, itemSnapshotList.size)
    }
}

fun LocalDate.Companion.nearFuture(
    startAt: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.UTC)
        .date,
    random: Int = Random.nextInt(52),
): LocalDate = startAt.plus(random, DateTimeUnit.WEEK)

@OptIn(ExperimentalUuidApi::class)
private fun tallyConf(location: String, dateStart: LocalDate) = ApiEvent(
    id = "${Uuid.random()}",
    name = "TallyConf $location '${"${dateStart.year}".substring(2)}",
    website = "https://tally.ashdavies.dev/",
    location = location,
    imageUrl = null,
    status = null,
    online = false,
    dateStart = "$dateStart",
    dateEnd = "${dateStart.plus(Random.nextInt(3), DateTimeUnit.DAY)}",
    cfp = null,
)
