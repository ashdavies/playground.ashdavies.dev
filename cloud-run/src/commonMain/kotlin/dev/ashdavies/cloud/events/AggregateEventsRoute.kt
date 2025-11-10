package dev.ashdavies.cloud.events

import com.google.cloud.firestore.CollectionReference
import dev.ashdavies.asg.AsgConference
import dev.ashdavies.asg.AsgService
import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.cloud.CollectionWriter
import dev.ashdavies.cloud.Identifier
import dev.ashdavies.cloud.decodeFromSnapshot
import dev.ashdavies.cloud.google.await
import dev.ashdavies.cloud.toApiConference
import dev.ashdavies.http.common.models.ApiConference
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.binding
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

private const val DEFAULT_ORDER_BY = "dateStart"

@ContributesIntoSet(AppScope::class, binding<CloudRunRoute>())
internal class AggregateEventsRoute @Inject constructor(
    @param:Named("events") private val collectionReference: CollectionReference,
    private val asgService: AsgService,
    private val identifier: Identifier<AsgConference>,
) : CloudRunRoute {

    override fun Routing.invoke() = post("/events:aggregate") {
        val collectionWriter = CollectionWriter(
            reference = collectionReference,
            identifier = ApiConference::id,
        )

        val snapshot = collectionReference
            .orderBy(DEFAULT_ORDER_BY)
            .await()

        collectionWriter.invoke(
            oldValue = Json.decodeFromSnapshot(snapshot),
            newValue = asgService { it.toApiConference(identifier(it)) },
            context = Dispatchers.IO,
        )

        call.respond(HttpStatusCode.OK)
    }
}

@ContributesTo(AppScope::class)
internal interface AggregateEventsProviders {

    @Provides
    fun asgService(httpClient: HttpClient): AsgService {
        return AsgService(httpClient)
    }

    @Provides
    fun identifier(): Identifier<AsgConference> {
        return Identifier()
    }
}
