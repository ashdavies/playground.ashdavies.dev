package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.google.auth.oauth2.ServiceAccountCredentials
import io.ashdavies.playground.compose.Provides
import java.net.URL
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

internal abstract class Algorithm(name: String, description: String) : JwtAlgorithm(name, description) {
    class Provider(private val publicKeys: JwkProvider, private val privateKey: RSAPrivateKey) : RSAKeyProvider {
        constructor(credentials: ServiceAccountCredentials) : this(
            publicKeys = UrlJwkProvider(URL(AppCheckConstants.APP_CHECK_PUBLIC_KEY)),
            privateKey = credentials.privateKey as RSAPrivateKey,
        )

        override fun getPublicKeyById(keyId: String): RSAPublicKey = publicKeys.get(keyId).publicKey as RSAPublicKey
        override fun getPrivateKey(): RSAPrivateKey = privateKey
        override fun getPrivateKeyId(): String? = null
    }
}

@Provides
@Composable
internal fun rememberAlgorithm(credentials: ServiceAccountCredentials = rememberGoogleCredentials()): JwtAlgorithm {
    return remember(credentials) { JwtAlgorithm.RSA256(Algorithm.Provider(credentials)) }
}
