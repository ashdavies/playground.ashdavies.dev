package io.ashdavies.aggregator

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import io.ashdavies.playground.aggregator.BuildConfig
import okhttp3.OkHttpClient

private const val HEADER_NAME = "Authorization"
private const val HEADER_PREFIX = "Bearer"

public fun ApolloClient(serverUrl: String): ApolloClient = ApolloClient.Builder()
    .addHttpHeader(HEADER_NAME, "$HEADER_PREFIX ${BuildConfig.GITHUB_TOKEN}")
    .okHttpClient(OkHttpClient())
    .serverUrl(serverUrl)
    .build()
