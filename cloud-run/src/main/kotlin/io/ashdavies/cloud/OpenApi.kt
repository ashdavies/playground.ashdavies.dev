package io.ashdavies.cloud

import com.google.cloud.storage.BlobId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal fun Route.openApi(path: String = "openapi") {
    get("$path/documentation.yaml") {
        val openApiConfig = storage[BlobId.of("playground-runtime", "openapi_config.yml")]

        when (openApiConfig != null) {
            true -> call.respond(String(openApiConfig.getContent()))
            false -> call.respond(HttpStatusCode.NotFound)
        }
    }
}
