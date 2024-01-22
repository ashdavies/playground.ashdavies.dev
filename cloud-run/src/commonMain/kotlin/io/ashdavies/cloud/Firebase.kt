package io.ashdavies.cloud

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import io.ashdavies.check.appCheck
import io.ashdavies.http.AppCheckToken
import io.ashdavies.http.common.models.AppCheckToken
import io.ashdavies.http.common.models.AuthResult
import io.ashdavies.http.common.models.DecodedToken
import io.ashdavies.http.common.models.SignInRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

private const val SIGNUP_ENDPOINT = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken"

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

internal fun Route.firebase(client: HttpClient) = route("/firebase") {
    post("/auth") {
        val firebaseAuth = FirebaseAuth.getInstance(firebaseApp)
        val accountRequest = call.receive<SignInRequest>()
        val customToken = firebaseAuth.createCustomToken(
            accountRequest.uid,
        )

        val authResponse = client.post(SIGNUP_ENDPOINT) {
            setBody(mapOf("token" to customToken, "returnSecureToken" to "true"))
            parameter("key", call.request.header("X-API-Key"))
        }.body<AuthResult>()

        call.respond(authResponse)
    }

    post("/token") {
        val appCheck = firebaseApp.appCheck(client)
        val appCheckRequest = call.receive<io.ashdavies.http.common.models.FirebaseApp>()
        val appCheckToken = appCheck.createToken(
            appId = appCheckRequest.appId,
            mapper = ::AppCheckToken,
        )

        call.respond(appCheckToken)
    }

    put("/token:verify") {
        val appCheck = firebaseApp.appCheck(client)
        val appCheckToken = requireNotNull(call.request.header(HttpHeaders.AppCheckToken)) {
            "Request is missing app check token header"
        }

        val decodedToken = appCheck.verifyToken(
            token = appCheckToken,
            mapper = ::DecodedToken,
        )

        call.respond(decodedToken)
    }
}
