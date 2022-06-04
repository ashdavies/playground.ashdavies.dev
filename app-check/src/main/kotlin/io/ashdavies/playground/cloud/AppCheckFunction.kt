package io.ashdavies.playground.cloud

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.firebase.FirebaseFunction

internal class AppCheckFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String = "Hello World"
}
