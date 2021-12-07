package io.ashdavies.playground.events

import com.google.firebase.cloud.FirestoreClient

private const val COLLECTION_PATH = "events"

fun CollectionReference() =
    FirestoreClient
        .getFirestore()
        .collection(COLLECTION_PATH)
