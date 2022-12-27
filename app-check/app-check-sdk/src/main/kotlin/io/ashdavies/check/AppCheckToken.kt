package io.ashdavies.check

import kotlinx.serialization.Serializable

@Serializable
public sealed class AppCheckToken {

    @Serializable
    public class Request(
        public val customToken: String,
        public val projectId: String,
        public val appId: String,
    ) : AppCheckToken()

    @Serializable
    public sealed class Response : AppCheckToken() {

        @Serializable
        public data class Normalised(
            val ttlMillis: Int,
            val token: String,
        ) : Response()

        @Serializable
        public data class Raw(
            val token: String,
            val ttl: String,
        ) : Response()
    }
}
