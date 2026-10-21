package dev.ashdavies.cloud.events

import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.cloud.appcheck.appCheckAuthentication
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

@Inject
@ContributesIntoSet(AppScope::class)
internal class AggregateEventsRoute(
    private val aggregateProviders: Map<EventAggregateSource, EventAggregateProvider>,
    private val firebaseEventsRoute: FirebaseEventWriter,
) : CloudRunRoute {

    override fun Routing.invoke() = appCheckAuthentication {
        post("/events:aggregate") {
            aggregateProviders.forEach { (source, provider) ->
                firebaseEventsRoute.write(source, provider())
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}
