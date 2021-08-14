package io.ashdavies.playground.events

import com.apollographql.apollo.ApolloClient
import com.google.cloud.firestore.CollectionReference
import com.google.firebase.cloud.FirestoreClient
import io.ashdavies.playground.Graph
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.yaml.Yaml

private const val COLLECTION_PATH = "events"
private const val GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

internal val Graph<*>.apolloClient: ApolloClient
    get() = ApolloClient.builder()
        .serverUrl(GRAPHQL_ENDPOINT)
        .build()

internal val Graph<*>.collectionReference: CollectionReference
    get() = FirestoreClient
        .getFirestore()
        .collection(COLLECTION_PATH)

internal val Graph<*>.gitHubService: GitHubService
    get() = GitHubService(apolloClient, Yaml)