package io.ashdavies.cloud

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import io.ashdavies.check.appCheck
import io.ashdavies.cloud.operations.FirebaseAuthOperation
import io.ashdavies.cloud.operations.FirebaseTokenOperation
import io.ashdavies.cloud.operations.VerifyTokenOperation
import io.ktor.client.HttpClient
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

internal val firebaseApp: FirebaseApp by lazy(LazyThreadSafetyMode.NONE) {
    when (val serviceAccountId = BuildConfig.GOOGLE_SERVICE_ACCOUNT_ID) {
        null -> FirebaseApp.initializeApp()
        else -> {
            val firebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setServiceAccountId(serviceAccountId)
                .build()

            FirebaseApp.initializeApp(firebaseOptions)
        }
    }
}

internal fun Route.firebase(httpClient: HttpClient) = route("/firebase") {
    val appCheck = firebaseApp.appCheck(httpClient)

    val firebaseAuth = FirebaseAuthOperation(
        firebaseAuth = FirebaseAuth.getInstance(firebaseApp),
        httpClient = httpClient,
    )

    val firebaseToken = FirebaseTokenOperation(appCheck)
    val verifyToken = VerifyTokenOperation(appCheck)

    post("/auth") { firebaseAuth(call) }

    post("/token") { firebaseToken(call) }

    put("/token:verify") { verifyToken(call) }
}
