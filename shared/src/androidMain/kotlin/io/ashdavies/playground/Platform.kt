package io.ashdavies.playground

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.datetime.toCalendar
import kotlinx.datetime.LocalDate

actual object Platform {
    actual val platform: String get() = "Android $SDK_INT"

    fun getEvent(): Event = TODO()
}
