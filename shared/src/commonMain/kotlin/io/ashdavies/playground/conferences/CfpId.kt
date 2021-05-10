package io.ashdavies.playground.conferences

import io.ashdavies.playground.database.ColumnAdapter

inline class CfpId(val value: Long) {

    companion object {

        val Adapter = ColumnAdapter(::CfpId, CfpId::value)
    }
}
