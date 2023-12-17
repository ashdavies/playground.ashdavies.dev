package io.ashdavies.playground

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable

public abstract class ComposeActivity(
    private val edgeToEdge: () -> Boolean = { true },
    private val strictMode: Context.() -> Boolean = { isDebuggable() },
    private val content: @Composable ComponentActivity.(Bundle?) -> Unit,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (edgeToEdge()) {
            enableEdgeToEdge()
        }

        if (strictMode()) {
            enableStrictMode()
        }

        setContent {
            content(savedInstanceState)
        }
    }
}

private fun Context.isDebuggable(): Boolean {
    return applicationInfo.flags != 0 and ApplicationInfo.FLAG_DEBUGGABLE
}

private fun enableStrictMode(penaltyDeath: Boolean = false) {
    val policy = StrictMode.ThreadPolicy.Builder()
        .also { if (penaltyDeath) it.penaltyDeath() else it }
        .detectAll()
        .penaltyLog()
        .build()

    StrictMode.setThreadPolicy(policy)
}
