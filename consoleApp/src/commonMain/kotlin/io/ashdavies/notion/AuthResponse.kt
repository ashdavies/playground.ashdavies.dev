package io.ashdavies.notion

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
internal val AuthResponseOwnerAdapter = object : ColumnAdapter<AuthResponse.Owner, String> {

    override fun decode(databaseValue: String): AuthResponse.Owner =
        Json.decodeFromString(databaseValue)

    override fun encode(value: AuthResponse.Owner): String =
        Json.encodeToString(value)
}
