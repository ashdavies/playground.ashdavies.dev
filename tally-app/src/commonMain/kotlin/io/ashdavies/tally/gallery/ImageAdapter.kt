package io.ashdavies.tally.gallery

import app.cash.sqldelight.ColumnAdapter
import io.ashdavies.tally.files.Path
import kotlin.uuid.Uuid

internal fun imageAdapter() = Image.Adapter(
    uuidAdapter = uuidAdapter(),
    pathAdapter = pathAdapter(),
)

private fun uuidAdapter() = object : ColumnAdapter<Uuid, String> {
    override fun decode(databaseValue: String) = Uuid.parse(databaseValue)
    override fun encode(value: Uuid): String = value.toString()
}

private fun pathAdapter() = object : ColumnAdapter<Path, String> {
    override fun decode(databaseValue: String): Path = Path(databaseValue)
    override fun encode(value: Path): String = value.toString()
}
