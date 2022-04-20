package io.ashdavies.playground

public actual suspend fun getAccessToken(provider: OAuthProvider): AccessToken = throw IllegalStateException()
