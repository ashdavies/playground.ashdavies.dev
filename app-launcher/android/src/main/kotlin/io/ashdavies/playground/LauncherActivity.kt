package io.ashdavies.playground

import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.defaultComponentContext

private const val INTENT_EXTRA_ROUTE = "route"

internal class LauncherActivity : KotlinActivity({
    val route = enumValueOfOrNull<LauncherRoute>(intent.getStringExtra(INTENT_EXTRA_ROUTE))

    setDecorFitsSystemWindows(window, true)
    setContent { LauncherApp(defaultComponentContext(), route) }
})

private inline fun <reified T : Enum<T>> enumValueOfOrNull(value: String?): T? {
    val term = value?.lowercase() ?: return null
    return enumValues<T>().first {
        it.name.lowercase() == term
    }
}
