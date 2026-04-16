package dev.ashdavies.playground

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        enableStrictMode()

        setContent {
            ConferenceApp(this)
        }
    }
}

private fun enableStrictMode(penaltyDeath: Boolean = false) {
    val policy = StrictMode.ThreadPolicy.Builder()
        .also { if (penaltyDeath) it.penaltyDeath() else it }
        .detectAll()
        .penaltyLog()
        .build()

    StrictMode.setThreadPolicy(policy)
}
