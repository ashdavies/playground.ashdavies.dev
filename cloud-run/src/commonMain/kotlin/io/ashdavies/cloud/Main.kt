package io.ashdavies.cloud

import com.google.firebase.auth.FirebaseAuth
import io.ashdavies.aggregator.AsgService
import io.ashdavies.check.appCheck
import io.ashdavies.cloud.operations.AggregateEventsOperation
import io.ashdavies.cloud.operations.FirebaseAuthOperation
import io.ashdavies.cloud.operations.FirebaseTokenOperation
import io.ashdavies.cloud.operations.UpcomingEventsOperation
import io.ashdavies.cloud.operations.VerifyTokenOperation
import io.ashdavies.http.common.models.Event
import io.ktor.http.HttpHeaders
import io.ktor.serialization.Configuration
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.CompressionConfig
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.defaultheaders.DefaultHeadersConfig
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

public fun main() {
    val server = embeddedServer(
        module = Application::main,
        factory = CIO,
        port = 8080,
    )

    server.start(wait = true)
}

internal fun Application.main() {
    val eventsCollection = firestore.collection("events")
    val appCheck = firebaseApp.appCheck(httpClient)

    install(DefaultHeaders, DefaultHeadersConfig::headers)
    install(Compression, CompressionConfig::default)
    install(ContentNegotiation, Configuration::json)
    install(ConditionalHeaders)
    install(CallLogging)

    routing {
        val upcomingEvents = UpcomingEventsOperation(eventsCollection)

        val aggregateEvents = AggregateEventsOperation(
            collectionReference = eventsCollection,
            collectionWriter = CollectionWriter(eventsCollection, Event::id),
            asgService = AsgService(httpClient),
            identifier = Identifier(),
        )

        val firebaseAuth = FirebaseAuthOperation(
            firebaseAuth = FirebaseAuth.getInstance(firebaseApp),
            httpClient = httpClient,
        )

        val firebaseToken = FirebaseTokenOperation(appCheck)
        val verifyToken = VerifyTokenOperation(appCheck)

        get("/events/upcoming") { upcomingEvents(call) }

        post("/events:aggregate") { aggregateEvents(call) }

        post("/firebase/auth") { firebaseAuth(call) }

        post("/firebase/token") { firebaseToken(call) }

        put("/firebase/token:verify") { verifyToken(call) }

        get("/hello") {
            call.respond("Hello, World!")
        }

        staticResources(
            remotePath = "/.well-known/",
            basePackage = "well-known",
        )
    }
}

private fun DefaultHeadersConfig.headers() {
    header(HttpHeaders.Server, System.getProperty("os.name"))
}
