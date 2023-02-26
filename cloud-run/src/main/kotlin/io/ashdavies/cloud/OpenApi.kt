package io.ashdavies.cloud

import com.google.cloud.storage.BlobId
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.routing.Route
import kotlin.io.path.createTempFile
import kotlin.io.path.pathString

internal fun Route.openApi() {
    val openApiConfigBlob = storage[BlobId.of("playground-runtime", "openapi_config.json")]
    val openApiConfig = createTempFile().also(openApiConfigBlob::downloadTo)

    openAPI(
        swaggerFile = openApiConfig.pathString,
        path = "openapi",
    )
}
