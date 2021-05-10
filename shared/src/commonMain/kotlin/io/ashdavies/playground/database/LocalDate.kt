/** Copyright © 2020 Robert Bosch GmbH. All rights reserved. */
package io.ashdavies.playground.database

import kotlinx.datetime.LocalDate

private val LocalDateAdapter = ColumnAdapter(LocalDate.Companion::parse, LocalDate::toString)

internal val LocalDate.Companion.Adapter
    get() = LocalDateAdapter
