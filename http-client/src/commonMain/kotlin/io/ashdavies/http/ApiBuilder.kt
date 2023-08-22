package io.ashdavies.http

import io.ashdavies.generated.infrastructure.ApiClient

public fun <T : ApiClient> buildApi(block: () -> T): T = block().apply {
    setApiKey(Environment.require("PLAYGROUND_API_KEY"))
}
