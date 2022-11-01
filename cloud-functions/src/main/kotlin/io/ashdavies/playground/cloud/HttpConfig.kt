package io.ashdavies.playground.cloud

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod

public data class HttpConfig(
    val accept: ContentType?,
    val allow: HttpMethod,
) {
    public companion object {
        public val Get: HttpConfig = HttpConfig(
            allow = HttpMethod.Get,
            accept = null,
        )

        public val Post: HttpConfig = HttpConfig(
            accept = ContentType.Application.Json,
            allow = HttpMethod.Post,
        )
    }
}
