package io.ashdavies.check

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.internal.CryptoSigners

private val internalCryptoSigner = Class
    .forName("com.google.firebase.auth.internal.CryptoSigner")

private val declaredGetCryptoSigner = CryptoSigners::class.java
    .getMethod("getCryptoSigner", FirebaseApp::class.java)
    .apply { isAccessible = true }

private val declaredSignValue = internalCryptoSigner
    .getDeclaredMethod("sign", ByteArray::class.java)
    .apply { isAccessible = true }

private val declaredGetAccount = internalCryptoSigner
    .getDeclaredMethod("getAccount")
    .apply { isAccessible = true }

public interface CryptoSigner {
    public suspend fun sign(value: ByteArray): ByteArray
    public fun getAccount(): String
}

internal fun CryptoSigner(firebaseApp: FirebaseApp): CryptoSigner = object : CryptoSigner {
    val instance = declaredGetCryptoSigner(null, firebaseApp)

    override suspend fun sign(value: ByteArray): ByteArray {
        return declaredSignValue(instance, value) as ByteArray
    }

    override fun getAccount(): String {
        return declaredGetAccount(instance) as String
    }
}
