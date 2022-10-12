package io.ashdavies.check

import kotlinx.serialization.Serializable

@Serializable
public sealed class AppCheckToken {

    @Serializable
    public sealed class Request : AppCheckToken() {

        @Serializable
        public data class Raw(
            val projectId: String,
            val appId: String,
        ) : Request()

        @Serializable
        public data class Processed(
            val customToken: String,
            val projectId: String,
            val appId: String,
        ) : Request()
    }

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
