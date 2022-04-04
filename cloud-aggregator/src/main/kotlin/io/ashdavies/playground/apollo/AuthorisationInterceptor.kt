package io.ashdavies.playground.apollo

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val HEADER_NAME = "Authorization"
private const val HEADER_PREFIX = "Bearer"

internal class AuthorisationInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed {
        addHeader(HEADER_NAME, "$HEADER_PREFIX $token")
    }
}

private fun Interceptor.Chain.proceed(
    block: Request.Builder.() -> Unit
): Response = request()
    .newBuilder()
    .apply(block)
    .build()
    .let(::proceed)
