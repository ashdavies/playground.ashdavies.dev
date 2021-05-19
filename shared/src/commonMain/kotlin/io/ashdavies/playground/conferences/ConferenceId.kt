package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.ColumnAdapter

inline class ConferenceId(val value: String) {

    companion object {

        val Adapter = ColumnAdapter(::ConferenceId, ConferenceId::value)
    }
}
