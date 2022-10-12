package io.ashdavies.playground.cloud

import java.net.HttpURLConnection

public class HttpException(public val code: Int, override val message: String, cause: Throwable?) : Exception(cause) {
    public companion object {
        public fun InvalidArgument(message: String, cause: Throwable? = null): HttpException {
            return HttpException(HttpURLConnection.HTTP_BAD_REQUEST, "InvalidArgument: $message", cause)
        }

        public fun Forbidden(message: String, cause: Throwable? = null): HttpException {
            return HttpException(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden: $message", cause)
        }

        public fun PermissionDenied(message: String, cause: Throwable? = null): HttpException {
            return HttpException(HttpURLConnection.HTTP_FORBIDDEN, "PermissionDenied: $message", cause)
        }
    }
}

