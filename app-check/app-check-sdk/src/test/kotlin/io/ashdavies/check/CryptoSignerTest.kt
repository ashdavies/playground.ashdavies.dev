package io.ashdavies.check

import com.google.auth.oauth2.ImpersonatedCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.internal.EmulatorCredentials
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
internal class CryptoSignerTest {

    @Test
    fun `should create crypto signer with GitHub OpenID Connect service account`() = runTest {
        val serviceAccount = "gh-oidc@playground-1a136.iam.gserviceaccount.com"
        val cryptoSigner = CryptoSigner(firebaseApp)

        assertEquals(serviceAccount, cryptoSigner.getAccount())
    }

    @Test
    fun `should refresh access token with impersonated credentials`() = runTest {
        val firebaseOptions = firebaseApp.options.toBuilder()
            .setServiceAccountId("gh-oidc@playground-1a136.iam.gserviceaccount.com")
            .setCredentials(ImpersonatedCredentials.create())
            .build()

        val firebaseApp = FirebaseApp.initializeApp(firebaseOptions, "[SECONDARY]")
        val cryptoSigner = CryptoSigner(firebaseApp)
        val randomUuid = "${UUID.randomUUID()}"

        assertNotNull(cryptoSigner.sign(randomUuid.toByteArray()))
    }
}
