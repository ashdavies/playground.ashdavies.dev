package io.ashdavies.cloud

import com.google.cloud.storage.BlobId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlin.io.path.createTempFile
import kotlin.io.path.pathString

internal fun Route.openApi(path: String = "openapi") {
    val openApiConfig = storage[BlobId.of("playground-runtime", "openapi_config.json")]
    if (openApiConfig == null) get(path) { call.respond(HttpStatusCode.NotFound) }
    else {
        val swaggerFile = createTempFile()
            .also(openApiConfig::downloadTo)
            .pathString

        swaggerUI(
            swaggerFile = swaggerFile,
            path = path,
        )
    }
}
