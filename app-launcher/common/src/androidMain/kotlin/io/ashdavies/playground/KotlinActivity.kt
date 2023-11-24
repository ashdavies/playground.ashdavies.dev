package io.ashdavies.playground

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

private typealias OnCreateAction = ComponentActivity.(savedInstanceState: Bundle?) -> Unit

public abstract class KotlinActivity(
    private val edgeToEdge: Boolean = true,
    private val action: OnCreateAction,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (edgeToEdge) {
            enableEdgeToEdge()
        }

        if (isDebuggable()) {
            enableStrictMode()
        }

        action(savedInstanceState)
    }
}

private fun Context.isDebuggable(): Boolean {
    return applicationInfo.flags != 0 and ApplicationInfo.FLAG_DEBUGGABLE
}

private fun enableStrictMode() {
    val policy = StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyDeath()
        .penaltyLog()
        .build()

    StrictMode.setThreadPolicy(policy)
}
