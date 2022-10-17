package io.ashdavies.check

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

private val MOBILE_SDK_APP_ID: String
    get() = requireNotNull(System.getenv("MOBILE_SDK_APP_ID"))

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppCheckFunctionTest {

    @Test
    fun `should return app check token for given credentials`() = startServer<AppCheckFunction> { client ->
        val request: HttpResponse = client.get { parameter("appId", MOBILE_SDK_APP_ID) }

        assertEquals(HttpStatusCode.OK, request.status)
    }

    @Test
    fun `client id should match service account`() {
        val credentials = FirebaseApp
            .initializeApp()
            .credentials

        assertEquals(credentials.clientId, System.getenv("GOOGLE_SERVICE_ACCOUNT_ID"))
    }
}
