package dev.ashdavies.cloud

import dev.ashdavies.asg.AsgConference
import dev.ashdavies.http.common.models.DomainEvent
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import okio.Buffer
import okio.ByteString.Companion.encodeUtf8
import kotlin.time.Instant
import kotlin.uuid.Uuid

public fun AsgConference.toDomainEvent(): DomainEvent = DomainEvent(
    uuid = uuid(),
    name = name,
    website = website,
    location = location,
    dateStart = dateStart,
    dateEnd = dateEnd,
)

public fun AsgConference.uuid(): Uuid = uuid(
    timestamp = dateStart.atStartOfDayIn(TimeZone.UTC),
    content = "$name\u0000$location\u0000$dateStart",
)

public fun uuid(timestamp: Instant, content: String): Uuid {
    val buffer = Buffer().write(content.encodeUtf8().sha256())
    val epochMillis = timestamp.toEpochMilliseconds()

    val msb = ((epochMillis and 0x0000_FFFF_FFFF_FFFFL) shl 16) or 0x7000L or (buffer.readLong() and 0x0FFFL)
    val lsb = (buffer.readLong() and 0x3FFF_FFFF_FFFF_FFFFL) or Long.MIN_VALUE

    return Uuid.fromLongs(msb, lsb)
}

public fun AsgConference.toAttrs(): Map<String, String> = buildMap {
    online?.let { put("online", "$it") }
    status?.let { put("status", it) }

    cfp?.let { cfp ->
        put("cfpStart", cfp.start)
        put("cfpEnd", cfp.end)

        cfp.site?.let { put("cfpSite", it) }
    }
}
