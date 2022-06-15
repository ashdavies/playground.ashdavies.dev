package io.ashdavies.playground.aggregator

import com.apollographql.apollo.ApolloClient
import io.ashdavies.playground.apollo.AuthorisationInterceptor
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.yaml.Yaml
import okhttp3.OkHttpClient
import java.lang.System.getenv

private const val GITHUB_TOKEN_NAME = "GITHUB_TOKEN"
private const val GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

private fun AuthorisationInterceptor() =
    AuthorisationInterceptor(getenv(GITHUB_TOKEN_NAME))

private fun ApolloClient() =
    ApolloClient.builder()
        .serverUrl(GRAPHQL_ENDPOINT)
        .okHttpClient { addInterceptor(AuthorisationInterceptor()) }
        .build()

internal fun GitHubService() =
    GitHubService(ApolloClient(), Yaml)

private fun ApolloClient.Builder.okHttpClient(block: OkHttpClient.Builder.() -> Unit): ApolloClient.Builder =
    OkHttpClient.Builder()
        .apply(block)
        .build()
        .let(::okHttpClient)
