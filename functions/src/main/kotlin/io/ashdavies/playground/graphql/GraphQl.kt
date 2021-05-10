package io.ashdavies.playground.graphql

import kotlin.js.Promise

@JsNonModule
@JsModule("@octokit/graphql")
external object GraphQl {
    fun <T> graphql(query: Query, parameters: RequestParameters): Promise<T>
}

internal fun <T> GraphQl.graphql(query: Query, token: String): Promise<T> =
    graphql(query, RequestParameters(RequestHeaders("token $token")))

internal typealias Query = String

data class RequestParameters(
    val headers: RequestHeaders?
)

data class RequestHeaders(
    val authorization: String
)
