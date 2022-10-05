package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.defaultComponentContext

internal class EventsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, true)
        setContent { EventsApp(defaultComponentContext()) }
    }
}
