package io.ashdavies.check

import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.algorithms.Algorithm.RSA256
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.RSAKeyProvider
import java.net.URL
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

private val JwkProvider = UrlJwkProvider(URL("https://firebaseappcheck.googleapis.com/v1/jwks"))
private val PublicRsa = RSA256(RSAKeyProvider { JwkProvider[it].publicKey as RSAPublicKey })

private fun RSAKeyProvider(block: (keyId: String) -> RSAPublicKey): RSAKeyProvider = object : RSAKeyProvider {
    override fun getPublicKeyById(keyId: String): RSAPublicKey = block(keyId)
    override fun getPrivateKey(): RSAPrivateKey? = null
    override fun getPrivateKeyId(): String? = null
}

public class GoogleAlgorithm(private val signer: CryptoSigner) : RsaAlgorithm(PublicRsa) {
    override fun sign(contentBytes: ByteArray): ByteArray = signer.sign(contentBytes)
    override fun verify(jwt: DecodedJWT): Unit = from.verify(jwt)
}

public abstract class RsaAlgorithm(public val from: JwtAlgorithm) : JwtAlgorithm(from.name, "$from")
