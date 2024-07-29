package io.ashdavies.analytics

import androidx.compose.runtime.Composable

internal typealias OnClickWith<P> = (P) -> Unit

internal typealias OnClick = () -> Unit

@Composable
public fun OnClick(
    name: String,
    parameters: Map<String, Any>? = null,
    action: () -> Unit,
): OnClick {
    val analytics = LocalAnalytics.current

    return {
        analytics.logEvent(name, parameters)
        action()
    }
}

@Composable
public fun <P> OnClickWith(
    name: String,
    parameters: Map<String, Any>? = null,
    action: (P) -> Unit,
): OnClickWith<P> {
    val analytics = LocalAnalytics.current

    return {
        analytics.logEvent(name, parameters)
        action(it)
    }
}
