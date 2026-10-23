package dev.ashdavies.cloud.events

import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.cloud.appcheck.appCheckAuthentication
import dev.ashdavies.cloud.google.attrs
import dev.ashdavies.cloud.google.getAndAwait
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromMap
import kotlin.time.Clock

private const val DEFAULT_COLLECTION = "events"
private const val DEFAULT_ORDER_BY = "dateStart"
private const val DEFAULT_LIMIT = 50

@Inject
@ContributesIntoSet(AppScope::class)
internal class UpcomingEventsRoute(firebaseApp: FirebaseApp) : CloudRunRoute {

    private val firestore = FirestoreClient.getFirestore(firebaseApp)
    private val collectionReference = firestore.collection(DEFAULT_COLLECTION)

    @OptIn(ExperimentalSerializationApi::class)
    override fun Routing.invoke() = appCheckAuthentication {
        get("/events/upcoming") {
            val query = collectionReference
                .orderBy(DEFAULT_ORDER_BY)
                .startAt(call.request.queryParameters["startAt"] ?: todayAsString())
                .limit(call.request.queryParameters["limit"]?.toIntOrNull() ?: DEFAULT_LIMIT)
                .getAndAwait()

            call.respond(
                message = query.map { document ->
                    DomainWrapper(
                        value = Properties.decodeFromMap<DomainEvent>(document.data),
                        attrs = document.data.attrs,
                    )
                },
            )
        }
    }
}

private fun todayAsString(): String = Clock.System
    .todayIn(TimeZone.currentSystemDefault())
    .toString()
