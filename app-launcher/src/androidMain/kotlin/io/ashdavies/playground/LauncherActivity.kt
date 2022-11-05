package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.defaultComponentContext

private const val INTENT_EXTRA_ROUTE = "route"

private typealias OnCreateAction = ComponentActivity.(savedInstanceState: Bundle?) -> Unit

internal class LauncherActivity : KotlinActivity({
    val route = enumValueOfOrNull<LauncherRoute>(intent.getStringExtra(INTENT_EXTRA_ROUTE))

    setDecorFitsSystemWindows(window, true)
    setContent { LauncherScreen(defaultComponentContext(), route) }
})

internal abstract class KotlinActivity(private val action: OnCreateAction) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        action(savedInstanceState)
    }
}

private inline fun <reified T : Enum<T>> enumValueOfOrNull(value: String?): T? {
    val term = value?.lowercase() ?: return null
    return enumValues<T>().first {
        it.name.lowercase() == term
    }
}
