package io.ashdavies.analytics

import androidx.compose.runtime.Composable

internal typealias OnClickWith<P> = (P) -> Unit

internal typealias OnClick = () -> Unit

@Composable
public fun OnClick(
    name: String,
    parameters: Map<String, Any>? = null,
    analytics: RemoteAnalytics = LocalAnalytics.current,
    action: () -> Unit,
): OnClick = {
    analytics.logEvent(name, parameters)
    action()
}

@Composable
public fun <P> OnClickWith(
    name: String,
    parameters: Map<String, Any>? = null,
    analytics: RemoteAnalytics = LocalAnalytics.current,
    action: (P) -> Unit,
): OnClickWith<P> = {
    analytics.logEvent(name, parameters)
    action(it)
}

private fun RemoteAnalytics.logEvent(
    name: String,
    parameters: Map<String, Any>?,
) = logEvent(name) {
    parameters?.forEach { (key, value) ->
        param(key, value.toString())
    }
}
