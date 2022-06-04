package io.ashdavies.playground.firebase

import com.google.firebase.FirebaseApp
import io.ashdavies.playground.check.AppCheck
import io.ashdavies.playground.check.AppCheckClient
import io.ashdavies.playground.check.AppCheckTokenGenerator
import io.ashdavies.playground.credential.Credential
import io.ashdavies.playground.crypto.CryptoSigner

internal fun FirebaseApp.appCheck(): AppCheck {
    val credential = Credential.ServiceAccountCredential(
        serviceAccountId = options.serviceAccountId,
        clientEmail = "noreply@github.io",
    )

    return AppCheck(
        generator = AppCheckTokenGenerator(CryptoSigner.IamSigner(credential)),
        client = AppCheckClient()
    )
}
