package dev.ashdavies.cloud.hello

import dev.ashdavies.cloud.CloudRunRoute
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

@Inject
@ContributesIntoSet(AppScope::class, binding<CloudRunRoute>())
internal class HelloWorldRoute : CloudRunRoute {
    override fun Routing.invoke() = get("hello") {
        call.respond("Hello World")
    }
}
