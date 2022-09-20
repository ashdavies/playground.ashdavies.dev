package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.google.auth.ServiceAccountSigner
import io.ashdavies.playground.compose.Provides
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

private const val JWKS_URL = "https://firebaseappcheck.googleapis.com/v1/jwks"

private fun PublicKeyProvider(provider: JwkProvider = UrlJwkProvider(JWKS_URL)) = PublicKeyProvider {
    provider[it].publicKey as RSAPublicKey
}

private fun PublicKeyProvider(block: (keyId: String) -> RSAPublicKey) = object : RSAKeyProvider {
    override fun getPublicKeyById(keyId: String): RSAPublicKey = block(keyId)
    override fun getPrivateKey(): RSAPrivateKey? = null
    override fun getPrivateKeyId(): String? = null
}

@Suppress("OVERRIDE_DEPRECATION")
internal class GoogleAlgorithm(private val signer: ServiceAccountSigner) : RsaAlgorithm(RSA256(PublicKeyProvider())) {
    override fun sign(contentBytes: ByteArray): ByteArray = signer.sign(contentBytes)
    override fun verify(jwt: DecodedJWT) = from.verify(jwt)
}

internal abstract class RsaAlgorithm(val from: JwtAlgorithm) : JwtAlgorithm(from.name, "$from")

@Provides
@Composable
internal fun rememberAlgorithm(signer: ServiceAccountSigner = rememberAccountSigner()): JwtAlgorithm {
    return remember(signer) { GoogleAlgorithm(signer) }
}
