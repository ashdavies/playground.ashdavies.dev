package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import io.ashdavies.check.CryptoSigner
import io.ashdavies.check.GoogleAlgorithm

@Composable
internal fun rememberAlgorithm(
    signer: CryptoSigner = rememberCryptoSigner()
): Algorithm = remember(signer) {
    GoogleAlgorithm(signer)
}
