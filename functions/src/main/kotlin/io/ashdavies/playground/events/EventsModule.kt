package io.ashdavies.playground.events

import com.apollographql.apollo.ApolloClient
import com.google.cloud.firestore.CollectionReference
import com.google.firebase.cloud.FirestoreClient
import io.ashdavies.playground.Graph
import io.ashdavies.playground.apollo.AuthorisationInterceptor
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.yaml.Yaml
import okhttp3.OkHttpClient
import java.lang.System.getenv

private const val COLLECTION_PATH = "events"
private const val GITHUB_TOKEN_NAME = "GITHUB_TOKEN"
private const val GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

internal val Graph<*>.apolloClient: ApolloClient
    get() = ApolloClient.builder()
        .serverUrl(GRAPHQL_ENDPOINT)
        .okHttpClient { addInterceptor(AuthorisationInterceptor(getenv(GITHUB_TOKEN_NAME))) }
        .build()

internal val Graph<*>.collectionReference: CollectionReference
    get() = FirestoreClient
        .getFirestore()
        .collection(COLLECTION_PATH)

internal val Graph<*>.gitHubService: GitHubService
    get() = GitHubService(apolloClient, Yaml)

private fun ApolloClient.Builder.okHttpClient(block: OkHttpClient.Builder.() -> Unit): ApolloClient.Builder =
    okHttpClient(OkHttpClient.Builder().apply(block).build())
