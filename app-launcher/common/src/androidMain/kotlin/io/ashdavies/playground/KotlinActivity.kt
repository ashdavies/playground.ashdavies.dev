package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

private typealias OnCreateAction = ComponentActivity.(savedInstanceState: Bundle?) -> Unit

public abstract class KotlinActivity(
    private val edgeToEdge: Boolean = true,
    private val action: OnCreateAction,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (edgeToEdge) enableEdgeToEdge()
        action(savedInstanceState)
    }
}
