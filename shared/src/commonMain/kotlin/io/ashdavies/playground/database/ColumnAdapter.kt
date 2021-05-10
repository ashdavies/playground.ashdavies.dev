/** Copyright © 2020 Robert Bosch GmbH. All rights reserved. */
package io.ashdavies.playground.database

import com.squareup.sqldelight.ColumnAdapter

internal fun <T : Any, S> ColumnAdapter(
    decode: (databaseValue: S) -> T,
    encode: (value: T) -> S,
): ColumnAdapter<T, S> = object : ColumnAdapter<T, S> {
    override fun decode(databaseValue: S): T = decode(databaseValue)
    override fun encode(value: T): S = encode(value)
}
