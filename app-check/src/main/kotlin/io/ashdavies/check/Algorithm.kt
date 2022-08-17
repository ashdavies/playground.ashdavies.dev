package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.google.auth.ServiceAccountSigner
import io.ashdavies.playground.compose.Provides
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

private object EmptyKeyProvider : RSAKeyProvider {
    override fun getPublicKeyById(keyId: String): RSAPublicKey? = null
    override fun getPrivateKey(): RSAPrivateKey? = null
    override fun getPrivateKeyId(): String? = null
}

@Suppress("OVERRIDE_DEPRECATION")
internal class GoogleAlgorithm(private val signer: ServiceAccountSigner) : RsaAlgorithm() {
    override fun sign(contentBytes: ByteArray): ByteArray = signer.sign(contentBytes)
    override fun verify(jwt: DecodedJWT) {
        RSA256(EmptyKeyProvider).verify(jwt)
        throw IllegalStateException("Not Implemented")
    }
}

internal abstract class RsaAlgorithm(val from: JwtAlgorithm = RSA256(EmptyKeyProvider)) : JwtAlgorithm(from.name, "$from")

@Provides
@Composable
internal fun rememberAlgorithm(signer: ServiceAccountSigner = rememberAccountSigner()): JwtAlgorithm {
    return remember(signer) { GoogleAlgorithm(signer) }
}
