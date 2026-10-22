package dev.ashdavies.playground

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import dev.ashdavies.playground.event.Attendance
import dev.ashdavies.playground.event.Event
import dev.ashdavies.playground.event.EventAttrs
import dev.ashdavies.playground.gallery.Image
import dev.ashdavies.playground.gallery.Path
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

internal operator fun PlaygroundDatabase.Companion.invoke(driver: SqlDriver): PlaygroundDatabase = PlaygroundDatabase(
    driver = driver,
    attendanceAdapter = Attendance.Adapter(uuidAdapter()),
    eventAdapter = Event.Adapter(
        uuidAdapter = uuidAdapter(),
        dateStartAdapter = localDateAdapter(),
        dateEndAdapter = localDateAdapter(),
        attrsAdapter = EventAttrs.Adapter,
    ),
    imageAdapter = imageAdapter(),
)

private fun uuidAdapter() = object : ColumnAdapter<Uuid, String> {
    override fun decode(databaseValue: String) = Uuid.parse(databaseValue)
    override fun encode(value: Uuid): String = value.toString()
}

private fun localDateAdapter() = object : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String) = LocalDate.parse(databaseValue)
    override fun encode(value: LocalDate): String = value.toString()
}

private fun imageAdapter(): Image.Adapter = Image.Adapter(
    uuidAdapter = uuidAdapter(),
    pathAdapter = pathAdapter(),
)

private fun pathAdapter() = object : ColumnAdapter<Path, String> {
    override fun decode(databaseValue: String): Path = Path(databaseValue)
    override fun encode(value: Path): String = value.toString()
}
