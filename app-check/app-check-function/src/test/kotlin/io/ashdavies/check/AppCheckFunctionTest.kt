package io.ashdavies.check

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.IdentityPoolCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

private val FirebaseApp.credentials: IdentityPoolCredentials
    get() = FirebaseOptions::class.java
        .getDeclaredMethod("getCredentials")
        .also { it.isAccessible = true }
        .invoke(options) as IdentityPoolCredentials

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppCheckFunctionTest {

    private val mobileSdkAppId: String
        get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        val request: HttpResponse = client.get { parameter("appId", mobileSdkAppId) }

        assertEquals(HttpStatusCode.OK, request.status)
    }

    @Test
    fun `should print client identifier`() {
        val credentials = FirebaseApp
            .initializeApp()
            .credentials

        println("=== ClientId (${credentials.clientId}) ===")
    }

    @Test
    fun `should retrieve subject token`() {
        val credentials = FirebaseApp
            .initializeApp()
            .credentials

        println("=== SubjectToken (${credentials.retrieveSubjectToken()}) ===")
    }
}
