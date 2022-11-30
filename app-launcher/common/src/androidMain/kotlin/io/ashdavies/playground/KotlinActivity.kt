package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity

private typealias OnCreateAction = ComponentActivity.(savedInstanceState: Bundle?) -> Unit

public abstract class KotlinActivity(private val action: OnCreateAction) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        action(savedInstanceState)
    }
}
