package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.LocalHttpRequest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

@Composable
@OptIn(ExperimentalSerializationApi::class)
private fun rememberAppCheckRequest(
    httpRequest: HttpRequest = LocalHttpRequest.current
): AppCheckRequest = remember(httpRequest) {
    Json.decodeFromStream(httpRequest.inputStream)
}
