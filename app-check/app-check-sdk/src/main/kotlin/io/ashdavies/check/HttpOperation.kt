package io.ashdavies.check

import androidx.compose.runtime.Composable
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpScope
import kotlinx.coroutines.CoroutineScope

@Composable
public fun HttpScope.VerifiedHttpEffect(key: Any = Unit, block: suspend CoroutineScope.() -> String) {
    AppCheck(verify = true) { HttpEffect(key, block) }
}
