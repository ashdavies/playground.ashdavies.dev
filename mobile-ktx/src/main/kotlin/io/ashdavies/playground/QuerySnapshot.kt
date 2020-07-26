package io.ashdavies.playground

import com.google.firebase.firestore.QuerySnapshot

inline fun <reified T> QuerySnapshot.toObjects(): List<T> = toObjects(T::class.java)
