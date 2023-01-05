package io.ashdavies.check

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.internal.CryptoSigners

private val internalCryptoSigner = Class
    .forName("com.google.firebase.auth.internal.CryptoSigner")

private val getCryptoSigner = CryptoSigners::class.java
    .getMethod("getCryptoSigner", FirebaseApp::class.java)
    .apply { isAccessible = true }

private val signValue = internalCryptoSigner
    .getDeclaredMethod("sign", ByteArray::class.java)
    .apply { isAccessible = true }

private val getAccount = internalCryptoSigner
    .getDeclaredMethod("getAccount")
    .apply { isAccessible = true }

public interface CryptoSigner {
    public fun sign(value: ByteArray): ByteArray
    public fun getAccount(): String
}

internal fun CryptoSigner(firebaseApp: FirebaseApp): CryptoSigner = object : CryptoSigner {
    override fun sign(value: ByteArray): ByteArray = signValue(instance, value) as ByteArray
    override fun getAccount(): String = getAccount(instance) as String
    val instance = getCryptoSigner(null, firebaseApp)
}
