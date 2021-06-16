package io.ashdavies.playground.graphql

internal object GraphQl {
    suspend fun <T> graphql(query: Query, parameters: RequestParameters): T = TODO()
}

internal suspend fun <T> GraphQl.graphql(query: Query, token: String): T =
    graphql(query, RequestParameters(RequestHeaders("token $token")))

internal typealias Query = String

data class RequestParameters(
    val headers: RequestHeaders?
)

data class RequestHeaders(
    val authorization: String
)
