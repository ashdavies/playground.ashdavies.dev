package io.ashdavies.playground.cloud

import java.net.HttpURLConnection

public class HttpException(val code: Int, override val message: String, cause: Throwable? = null) : Exception(cause) {
    companion object {
        fun InvalidArgument(message: String, cause: Throwable? = null) =
            HttpException(HttpURLConnection.HTTP_BAD_REQUEST, "InvalidArgument: $message", cause)

        fun Forbidden(message: String, cause: Throwable? = null): HttpException =
            HttpException(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden: $message", cause)
    }
}
