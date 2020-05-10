package io.ashdavies.playground.extensions

import com.google.firebase.firestore.QuerySnapshot

internal inline fun <reified T> QuerySnapshot.toObjects() = toObjects(T::class.java)
