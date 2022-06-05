package io.ashdavies.playground.check

internal class AppCheck(
    private val client: AppCheckClient,
    private val generator: AppCheckTokenGenerator,
) : AppCheckInterface {

    /**
     * @see [app-check.ts](https://github.com/firebase/firebase-admin-node/blob/4e816f44a3/src/app-check/app-check.ts#L64)
     */
    override suspend fun createToken(appId: String, options: AppCheckTokenOptions?): AppCheckToken {
        return client.exchangeToken(generator.createCustomToken(appId, options), appId)
    }

    /**
     * @see [app-check.ts](https://github.com/firebase/firebase-admin-node/blob/4e816f44a3/src/app-check/app-check.ts#L79)
     */
    override suspend fun verifyToken(appCheckToken: String): VerifyAppCheckTokenResponse {
        TODO("Not yet implemented")
    }
}
