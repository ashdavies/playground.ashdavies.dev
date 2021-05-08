package io.ashdavies.playground.graphql

import kotlin.js.Promise

@JsNonModule
@JsModule("@octokit/graphql")
internal external object GraphQl {
    fun <T> graphql(query: Query, parameters: RequestParameters): Promise<T>
}

internal fun <T> GraphQl.graphql(query: Query, token: String): Promise<T> =
    graphql(query, RequestParameters(RequestHeaders("token $token")))

internal typealias Query = String

internal data class RequestParameters(
    val headers: RequestHeaders?
)

internal data class RequestHeaders(
    val authorization: String
)
