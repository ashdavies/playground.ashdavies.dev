package io.ashdavies.playground.check

import com.google.firebase.FirebaseApp

internal fun FirebaseApp.appCheck(): AppCheck = AppCheck(
    generator = AppCheckTokenGenerator(CryptoSigner()),
    client = AppCheckClient()
)
