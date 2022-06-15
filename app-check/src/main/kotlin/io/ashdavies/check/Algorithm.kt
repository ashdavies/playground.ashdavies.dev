package io.ashdavies.check

import com.auth0.jwk.JwkProvider
import com.auth0.jwt.interfaces.RSAKeyProvider
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

internal abstract class Algorithm(name: String, description: String) : JwtAlgorithm(name, description) {

    class Provider(private val publicKeys: JwkProvider, private val privateKey: RSAPrivateKey) : RSAKeyProvider {
        override fun getPublicKeyById(keyId: String): RSAPublicKey = publicKeys.get(keyId).publicKey as RSAPublicKey
        override fun getPrivateKey(): RSAPrivateKey = privateKey
        override fun getPrivateKeyId(): String? = null
    }

    companion object
}
