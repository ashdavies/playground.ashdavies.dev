package io.ashdavies.playground.cloud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.google.cloud.firestore.Firestore
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import io.ashdavies.playground.google.DocumentProvider

public val LocalFirebaseApp: ProvidableCompositionLocal<FirebaseApp> =
    compositionLocalOf { FirebaseApp.initializeApp() }

public val LocalHttpRequest: ProvidableCompositionLocal<HttpRequest> =
    compositionLocalOf { noLocalProvidedFor("LocalHttpRequest") }

public val LocalHttpResponse: ProvidableCompositionLocal<HttpResponse> =
    compositionLocalOf { noLocalProvidedFor("LocalHttpResponse") }

@Composable
public fun rememberDocumentProvider(path: String, firestore: Firestore = rememberFirestore()): DocumentProvider =
    remember(path) { DocumentProvider { firestore.collection(path) } }

@Composable
public fun rememberFirestore(firebaseApp: FirebaseApp = LocalFirebaseApp.current): Firestore =
    remember { FirestoreClient.getFirestore(firebaseApp) }

private fun noLocalProvidedFor(name: String): Nothing =
    error("CompositionLocal $name not present")

