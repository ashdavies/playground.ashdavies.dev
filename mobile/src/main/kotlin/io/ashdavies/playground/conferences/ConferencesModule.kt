package io.ashdavies.playground.conferences

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction.DESCENDING
import io.ashdavies.playground.database
import io.ashdavies.playground.github.ConferenceDatabase

internal fun database(
    context: Context
): ConferenceDatabase = context
    .applicationContext
    .database("GitHub.db")

internal fun firestore(
): FirebaseFirestore = getInstance()

internal fun service(
    query: Query = query()
): ConferencesService = ConferencesService(query)

internal fun query(
    firestore: FirebaseFirestore = firestore()
): Query = firestore
    .collection("conference")
    .orderBy("dateStart", DESCENDING)