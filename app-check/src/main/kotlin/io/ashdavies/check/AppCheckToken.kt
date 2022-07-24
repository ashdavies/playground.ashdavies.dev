package io.ashdavies.check

internal sealed interface AppCheckToken {
    sealed interface Request : AppCheckToken {
        data class Raw(val appId: String, val projectId: String) : Request
        data class Processed(val customToken: String, val projectId: String, val appId: String) : Request
    }

    sealed interface Response : AppCheckToken {
        data class Normalised(val token: String, val ttlMillis: Int) : Response
        data class Raw(val token: String, val ttl: String) : Response
    }
}
