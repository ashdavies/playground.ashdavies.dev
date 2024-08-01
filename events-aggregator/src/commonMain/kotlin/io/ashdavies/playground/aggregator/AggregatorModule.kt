package io.ashdavies.playground.aggregator

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import io.ashdavies.playground.apollo.AuthorisationInterceptor
import okhttp3.OkHttpClient

private const val GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

private fun AuthorisationInterceptor() = AuthorisationInterceptor(BuildConfig.GITHUB_TOKEN)

public fun ApolloClient(): ApolloClient = ApolloClient.Builder()
    .serverUrl(GRAPHQL_ENDPOINT)
    .okHttpClient { addInterceptor(AuthorisationInterceptor()) }
    .build()

private fun ApolloClient.Builder.okHttpClient(block: OkHttpClient.Builder.() -> Unit): ApolloClient.Builder =
    OkHttpClient.Builder()
        .apply(block)
        .build()
        .let(::okHttpClient)
