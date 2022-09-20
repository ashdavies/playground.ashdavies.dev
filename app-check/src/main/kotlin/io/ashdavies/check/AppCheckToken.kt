package io.ashdavies.check

import kotlinx.serialization.Serializable

@Serializable
internal sealed class AppCheckToken {

    @Serializable
    sealed class Request : AppCheckToken() {

        @Serializable
        data class Raw(val appId: String, val projectId: String) : Request()

        @Serializable
        data class Processed(val customToken: String, val projectId: String, val appId: String) : Request()
    }

    @Serializable
    sealed class Response : AppCheckToken() {

        @Serializable
        data class Normalised(val token: String, val ttlMillis: Int) : Response()

        @Serializable
        data class Raw(val token: String, val ttl: String) : Response()
    }
}
