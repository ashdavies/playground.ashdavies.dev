package io.ashdavies.playground.cloud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.cloud.firestore.Firestore
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import io.ashdavies.playground.google.DocumentProvider

public val LocalApplicationScope: ProvidableCompositionLocal<ApplicationScope> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalApplicationScope") }

public val LocalFirebaseApp: ProvidableCompositionLocal<FirebaseApp> =
    staticCompositionLocalOf { FirebaseApp.initializeApp() }

public val LocalHttpRequest: ProvidableCompositionLocal<HttpRequest> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalHttpRequest") }

public val LocalHttpResponse: ProvidableCompositionLocal<HttpResponse> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalHttpResponse") }

@Composable
public fun rememberDocumentProvider(path: String, firestore: Firestore = rememberFirestore()): DocumentProvider =
    remember(path) { DocumentProvider { firestore.collection(path) } }

@Composable
public fun rememberFirestore(firebaseApp: FirebaseApp = LocalFirebaseApp.current): Firestore =
    remember { FirestoreClient.getFirestore(firebaseApp) }

private fun noLocalProvidedFor(name: String): Nothing =
    error("CompositionLocal $name not present")

