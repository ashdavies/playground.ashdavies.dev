package io.ashdavies.playground

public expect suspend fun getAccessToken(provider: OAuthProvider = OAuthProvider.Google): AccessToken
