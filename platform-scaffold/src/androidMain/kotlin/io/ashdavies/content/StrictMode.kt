package io.ashdavies.content

import android.os.StrictMode

public fun enableStrictMode(penaltyDeath: Boolean = false) {
    val policy = StrictMode.ThreadPolicy.Builder()
        .also { if (penaltyDeath) it.penaltyDeath() else it }
        .detectAll()
        .penaltyLog()
        .build()

    StrictMode.setThreadPolicy(policy)
}
