package io.ashdavies.check

import androidx.compose.runtime.Composable
import io.ashdavies.playground.cloud.HttpEffect
import kotlinx.coroutines.CoroutineScope

@Composable
public fun VerifiedHttpEffect(key: Any = Unit, block: suspend CoroutineScope.() -> String) {
    AppCheck(verify = true) { HttpEffect(key, block) }
}
