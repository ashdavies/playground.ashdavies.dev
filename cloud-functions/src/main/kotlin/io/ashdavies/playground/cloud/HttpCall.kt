package io.ashdavies.playground.cloud

import androidx.compose.runtime.Composable
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse

internal typealias HttpCall = HttpScope.(HttpRequest, HttpResponse) -> Unit
internal typealias HttpComposable = @Composable HttpScope.() -> Unit
internal typealias HttpApplier = HttpFunction
